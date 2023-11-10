package com.vansoft.gps_tracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.vansoft.gps_tracker.databinding.FragmentMainBinding
import org.osmdroid.config.Configuration
import android.provider.Settings


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("HardwareIds")
    private fun settingsOsm(){
        Configuration.getInstance().load(
            activity as AppCompatActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = Settings.Secure.getString((activity as AppCompatActivity)
            .contentResolver, Settings.Secure.ANDROID_ID)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
