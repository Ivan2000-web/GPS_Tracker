package com.vansoft.gps_tracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vansoft.gps_tracker.location.LocationModel

class MainViewModel : ViewModel() {
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
}