package com.vansoft.gps_tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.vansoft.gps_tracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onNavButtonClick()
    }

    private fun onNavButtonClick(){
        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.id_home -> Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                R.id.id_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                R.id.id_tracks -> Toast.makeText(this, "Tracks", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }
}