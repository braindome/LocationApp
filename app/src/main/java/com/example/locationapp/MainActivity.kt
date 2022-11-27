package com.example.locationapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_LOCATION : Int = 1
    lateinit var locationProvider : FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(2000).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult : LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("!!!", "lat: ${location.latitude}, lng: ${location.longitude}")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        }


    }

    fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        locationProvider.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    fun stopLocationUpdates() {
        locationProvider.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
                // börja använda position
            }
        }
    }
}