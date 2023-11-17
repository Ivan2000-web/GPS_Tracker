package com.vansoft.gps_tracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vansoft.gps_tracker.databinding.ActivityMainBinding
import com.vansoft.gps_tracker.fragments.MainFragment
import com.vansoft.gps_tracker.fragments.SettingsFragment
import com.vansoft.gps_tracker.fragments.TracksFragment
import com.vansoft.gps_tracker.utils.DialogManager
import com.vansoft.gps_tracker.utils.openFragment
import com.vansoft.gps_tracker.utils.showToast


class MainActivity : AppCompatActivity() {
    companion object{
        private var permAttemptsCount = 0
    }

    private lateinit var binding: ActivityMainBinding
    private val requestCode = 11;
    private val permission =  Manifest.permission.ACCESS_FINE_LOCATION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("testActivity", "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onNavButtonClick()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){ //android 11+
              requestLocPermission()
            } else {
            openFragment(MainFragment.newInstance())
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("testActivity", "onResume")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("testActivity", "onDestroy")
    }

    private fun onNavButtonClick() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_settings -> openFragment(SettingsFragment())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
            }
            true
        }
    }

    private fun requestLocPermission() { //for android 11+
        if (checkPermissions()) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            openFragment(MainFragment.newInstance())
        }
    }

    override fun onRequestPermissionsResult(
        code: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(code, permissions, grantResults)
        when (code) {
            requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openFragment(MainFragment.newInstance())
                } else {
                    ++permAttemptsCount
                    if (permAttemptsCount == 2) {
                        showAlertDialog("Необходимо предоставить разрешения!",
                            "Для работы приложения необходимо предоставить разрешение на определение местоположения. " +
                                    "ПЕРЕЙТИ В НАСТРОЙКИ?",
                            object : DialogManager.Listener {
                                override fun onClick() {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", packageName, null)
                                    intent.data = uri
                                    startForResult.launch(intent)
                                }
                            }
                        )
                    } else {
                        showAlertDialog("Запрос разрешений на определение местоположения!",
                            "Для работы приложения необходимо подтвердить разрешения на определение местоположения. " +
                                    "Вы согласны предоставить разрешения?",
                            object : DialogManager.Listener {
                                override fun onClick() {
                                    recreate()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!checkPermissions()) {
            openFragment(MainFragment.newInstance())
        }
    }

    private fun showAlertDialog(str1: String, str2: String, listener: DialogManager.Listener) {
        val builder = android.app.AlertDialog.Builder(this)
        val dialog = builder.create()
        dialog.setTitle(str1)
        dialog.setMessage(str2)
        dialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "Да") { _, _ ->
            listener.onClick()
        }
        dialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE, "Нет") { _, _ ->
            showToast("Определение местоположения не включено!")
        }
        dialog.show()
    }

    private fun checkPermissions(): Boolean { //if true - permissions not granted
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }
}