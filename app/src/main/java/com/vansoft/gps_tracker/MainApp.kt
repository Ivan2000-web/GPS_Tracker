package com.vansoft.gps_tracker

import android.app.Application
import com.vansoft.gps_tracker.db.MainDb

class MainApp : Application() {
    val database by lazy { MainDb.getDatabase(this) }
}