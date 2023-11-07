package com.vansoft.gps_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.vansoft.gps_tracker.databinding.ActivityMainBinding
import com.vansoft.gps_tracker.fragments.MainFragment
import com.vansoft.gps_tracker.fragments.SettingsFragment
import com.vansoft.gps_tracker.fragments.TracksFragment
import com.vansoft.gps_tracker.utils.openFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onNavButtonClick()
        openFragment(MainFragment.newInstance())
    }

    private fun onNavButtonClick(){
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.id_home -> openFragment(MainFragment.newInstance())
                R.id.id_settings -> openFragment(SettingsFragment.newInstance())
                R.id.id_tracks -> openFragment(TracksFragment.newInstance())
            }
            true
        }

    }
}