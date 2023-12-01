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
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import com.vansoft.gps_tracker.MainApp
import com.vansoft.gps_tracker.MainViewModel
import com.vansoft.gps_tracker.R
import com.vansoft.gps_tracker.databinding.ViewTrackBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


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
            val polyline = getPolyline(it.geoPoints)
            map.overlays.add(polyline)
            setMarkers(polyline.actualPoints)
            goToPosition(polyline.actualPoints[0])
        }
    }

    private fun getPolyline(geoPoints: String): Polyline{
        val polyline = Polyline()
        val list = geoPoints.split("/")
        list.forEach{
            if(it.isEmpty()) return@forEach
            val points = it.split(",")
            polyline.addPoint(GeoPoint(points[0].toDouble(), points[1].toDouble()))
        }
        return polyline;
    }

    private fun goToPosition(startPosition: GeoPoint){
        binding.map.controller.zoomTo(15.0)
        binding.map.controller.animateTo(startPosition)
    }

    private fun setMarkers(list: List<GeoPoint>) = with(binding){
        val startMarker = Marker(map)
        val finishMarker = Marker(map)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        finishMarker.icon = getDrawable(requireContext(), R.drawable.ic_finish)
        startMarker.position = list[0]
        finishMarker.position = list[list.size - 1]
        map.overlays.add(startMarker)
        map.overlays.add(finishMarker)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewTrackFragment()
    }
}
