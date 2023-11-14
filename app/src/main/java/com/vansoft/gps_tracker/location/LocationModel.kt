package com.vansoft.gps_tracker.location

import org.osmdroid.util.GeoPoint

data class LocationModel(
    val speed: Float = 0.0f,
    val distance: Float = 0.0f,
    val geoPointsList: ArrayList<GeoPoint>
)
