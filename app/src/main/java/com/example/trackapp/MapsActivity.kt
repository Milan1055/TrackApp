package com.example.trackapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            getLocationUpdates()
            startLocationUpdates()
        }
        else
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST)


    }

    private fun getLocationUpdates() {
            locationRequest = LocationRequest()
            locationRequest.interval = 30000
            locationRequest.fastestInterval = 20000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult.locations.isNotEmpty()) {
                        val location = locationResult.lastLocation


//                    val uid = FirebaseAuth.getInstance().currentUser?.uid
//                    val rootRef =  FirebaseFirestore.getInstance()
//                    val usersRef = rootRef.collection("users")
//                    val uidRef = uid?.let { usersRef.document(it) }
//                    if (uidRef != null) {
//                        uidRef.get()
//                                .addOnSuccessListener { document ->
//                                    if (document != null) {
//                                        val latitude = document.getDouble("latitude")
//                                        val longitude = document.getDouble("longitude")
//                                        Log.d(TAG, ", " + location.latitude + location.longitude)
//                                    } else {
//                                        Log.d(TAG, "No such document")
//                                    }
//                                }
//                                .addOnFailureListener { exception ->
//                                    Log.d(TAG, "get failed with ", exception)
//                                }
//                    }




                        lateinit var databaseRef: DatabaseReference
                        databaseRef = Firebase.database.reference
                        val locationlogging = LocationLogging(location.latitude, location.longitude)
                        databaseRef.child("/userlocation").setValue(locationlogging)

                                .addOnSuccessListener {
                                    Toast.makeText(applicationContext, "Locations written into the database", Toast.LENGTH_LONG).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(applicationContext, "Error occured while writing the locations", Toast.LENGTH_LONG).show()
                                }



                        if (location != null) {
                            val latLng = LatLng(location.latitude, location.longitude)
                            val markerOptions = MarkerOptions().position(latLng)
                            map.addMarker(markerOptions)
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                }
            }
        }




    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, null)
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                map.isMyLocationEnabled = true
            } else { Toast.makeText(this, "User has not granted location access permission", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        getLocationAccess()
    }



}



                /**
                 * Manipulates the map once available.
                 * This callback is triggered when the map is ready to be used.
                 * This is where we can add markers or lines, add listeners or move the camera. In this case,
                 * we just add a marker near Sydney, Australia.
                 * If Google Play services is not installed on the device, the user will be prompted to install
                 * it inside the SupportMapFragment. This method will only be triggered once the user has
                 * installed Google Play services and returned to the app.
                 */


                    // Add a marker in Sydney and move the camera
                    //    val zoomLevel = 15f
                    //    val sydney = LatLng(-34.0, 151.0)
                    //    mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                    //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))






