package com.vansoft.gps_tracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.vansoft.gps_tracker.MainApp
import com.vansoft.gps_tracker.MainViewModel
import com.vansoft.gps_tracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration


class ViewTrackFragment : Fragment() {
    private lateinit var binding: ViewTrackBinding
    private val model: MainViewModel by activityViewModels{
        MainViewModel.ViewModelFactory((requireContext().applicationContext as MainApp).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsOsm()
        binding = ViewTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("HardwareIds")
    private fun settingsOsm() { //открываем карту
        Configuration.getInstance().load(
            activity as AppCompatActivity,
            activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
        )
        Configuration.getInstance().userAgentValue = Settings.Secure.getString(
            (activity as AppCompatActivity)
                .contentResolver, Settings.Secure.ANDROID_ID
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTrack()
    }

    private fun getTrack() = with(binding){
        model.currentTrack.observe(viewLifecycleOwner){
            val averageSpeed = "Average speed: ${it.averageSpeed} km/h"
            val distance = "Distance: ${it.distance} m"
            val date = "Date: ${it.date}"
            tvDate.text = date
            tvTime.text = it.time
            tvAverageSpeed.text = averageSpeed
            tvDistance.text = distance
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}
