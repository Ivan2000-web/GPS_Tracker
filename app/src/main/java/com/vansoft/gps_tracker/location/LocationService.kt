package com.vansoft.gps_tracker.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.vansoft.gps_tracker.MainActivity
import com.vansoft.gps_tracker.R
import org.osmdroid.util.GeoPoint


class LocationService : Service() {
    private var distance = 0.0f
    private var lastLocation: Location? = null
    private lateinit var locProvider: FusedLocationProviderClient
    private lateinit var locRequest: LocationRequest
    private lateinit var geoPointsList: ArrayList<GeoPoint>
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        startLocationUpdates()
        isRunning = true
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        geoPointsList = ArrayList()
        initLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        locProvider.removeLocationUpdates(locCallback)
    }

    private fun startNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val nChannel = NotificationChannel(
                CHANNEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
            nManager.createNotificationChannel(nChannel)
        }

        val nIntent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            10,
            nIntent,
            0
        )

        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        ).setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Tracker is running")
            .setContentIntent(pIntent)
            .build()
        startForeground(99, notification)
    }

    private val locCallback = object : LocationCallback() {
        override fun onLocationResult(lResult: LocationResult) {
            super.onLocationResult(lResult)
            val currentLocation = lResult.lastLocation
            if (lastLocation != null && currentLocation != null) {
                //if(currentLocation.speed > 0.2) distance += lastLocation?.distanceTo(currentLocation)!! //для реального устройства
                distance += lastLocation?.distanceTo(currentLocation)!!
                geoPointsList.add(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                val locModel = LocationModel(
                    currentLocation.speed,
                    distance,
                    geoPointsList
                )
                sendLocData(locModel)
            }
            lastLocation = currentLocation
        }
    }

    private fun sendLocData(locModel: LocationModel){
        val i = Intent(LOC_MODEL_INTENT)
        i.putExtra(LOC_MODEL_INTENT, locModel)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(i)
    }

    private fun initLocation() {
        locRequest = LocationRequest.create()
        locRequest.interval = 7000
        locRequest.fastestInterval = 5000
        locRequest.priority = PRIORITY_HIGH_ACCURACY
        locProvider = LocationServices.getFusedLocationProviderClient(baseContext)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        locProvider.requestLocationUpdates(
            locRequest,
            locCallback,
            Looper.myLooper()
            )
    }

    companion object{
        const val LOC_MODEL_INTENT = "loc_intent"
        const val CHANNEL_ID = "channel_1"
        var isRunning = false
        var startTime = 0L
    }
}