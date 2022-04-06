package br.edu.infnet.anotacoes

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<Button>(R.id.btn_get_location).setOnClickListener {
            fetchLocation()
        }

        findViewById<Button>(R.id.btn_list_location).setOnClickListener {
            goToLocationsActivity()
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {

            goToMapsActivity(it.latitude, it.longitude)
        }
    }

    private fun goToMapsActivity(lat: Double, long: Double) {
        var mapsActivity = Intent(this, MapsActivity::class.java)
        mapsActivity.putExtra("lat", lat)
        mapsActivity.putExtra("long", long)
        startActivity(mapsActivity)
    }

    private fun goToLocationsActivity() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            return
        }

        var locationsActivity = Intent(this, LocationActivity::class.java)
        startActivity(locationsActivity)
    }
}