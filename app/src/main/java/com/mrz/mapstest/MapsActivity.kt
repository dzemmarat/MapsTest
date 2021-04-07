package com.mrz.mapstest

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                       Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        // 1
        mMap.isMyLocationEnabled = true

        // 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        setUpMap()

        val saratov = LatLng(51.522630, 46.028044)
        mMap.addMarker(MarkerOptions()
            .position(saratov)
            .icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
            .title("Saratov"))

        val latLngOrigin = LatLng(10.3181466, 123.9029382) // Ayala
        val latLngDestination = LatLng(10.311795,123.915864) // SM City
        this.mMap!!.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))
        this.mMap!!.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))

//        val path: MutableList<List<LatLng>> = ArrayList()
//        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?origin=51.522630,46.028044&destination=51.526407,46.017116&key=<${R.string.google_maps_key}>"
//        val directionsRequest = object : StringRequest(Request.Method.GET, urlDirections, Response.Listener<String> {
//                response ->
//            val jsonResponse = JSONObject(response)
//            // Get routes
//            val routes = jsonResponse.getJSONArray("routes")
//            val legs = routes.getJSONObject(0).getJSONArray("legs")
//            val steps = legs.getJSONObject(0).getJSONArray("steps")
//            for (i in 0 until steps.length()) {
//                val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
//                path.add(PolyUtil.decode(points))
//            }
//            for (i in 0 until path.size) {
//                this.mMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED))
//            }
//        }, Response.ErrorListener {
//                _ ->
//        }){}
//        val requestQueue = Volley.newRequestQueue(this)
//        requestQueue.add(directionsRequest)

    }

    override fun onMarkerClick(p0: Marker?) = false

}