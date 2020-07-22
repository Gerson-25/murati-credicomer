package sv.com.credicomer.murati.ui.ride.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentMapBinding
import sv.com.credicomer.murati.ui.ride.PERMISSION_ID_COARSE_FINE_LOCATION
import sv.com.credicomer.murati.ui.ride.generateBitmapDescriptorFromRes
import sv.com.credicomer.murati.ui.ride.subscribeTopicNotifications
import sv.com.credicomer.murati.ui.ride.viewModels.MapViewModel
import sv.com.credicomer.murati.ui.ride.viewModels.ReservationViewModel
import timber.log.Timber


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var binding: FragmentMapBinding
    private lateinit var driverMaker: Marker
    private lateinit var userMaker: Marker
    private var googleMap: GoogleMap? = null
    private lateinit var viewModel:MapViewModel

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        viewModel=ViewModelProvider(this).get(MapViewModel::class.java)
        reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        getLastLocation()
        subscribeTopicNotifications("service")
        reservationViewModel.getLocation()
        setHasOptionsMenu(true)


        viewModel.getMapStat().observe(viewLifecycleOwner, Observer {

            if(it){
                binding.emptyStateRide2.visibility = View.GONE
            }else{
                binding.emptyStateRide2.visibility = View.VISIBLE
            }
            Timber.d("RIDE_STATUS %s","$it")



        })

        reservationViewModel.location.observe(viewLifecycleOwner, Observer {


            if (::driverMaker.isInitialized) {

                driverMaker.position = it
                updateCamera()

            }


        })


        return binding.root
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.forEach {
            it.isVisible = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        googleMap.isTrafficEnabled=true


        googleMap.uiSettings.setAllGesturesEnabled(false)
        googleMap.uiSettings.isZoomControlsEnabled=true

        // For showing a move to my location button
        //googleMap.isMyLocationEnabled = true

        // For dropping a marker at a point on the Map
        val unicomer = LatLng(13.698800, -89.226894)
        val pacifico = LatLng(13.698006, -89.235254)



        userMaker = googleMap.addMarker(MarkerOptions().position(pacifico).snippet("ME"))

        driverMaker = googleMap.addMarker(
            MarkerOptions().position(pacifico).title("Driver").snippet("Unicomer Driver").icon(
                generateBitmapDescriptorFromRes(requireContext(), R.drawable.ic_bus_in_map)
            )
        )

        updateCamera()

        /* // For zooming automatically to the location of the marker
         val cameraPosition = CameraPosition.Builder().target(unicomer).zoom(14f).build() //19
         googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))*/


    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


    private fun updateCamera() {

        val builder = LatLngBounds.Builder()
        builder.include(userMaker.position)
        builder.include(driverMaker.position)

        val bounds = builder.build()

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.20).toInt() // offset from edges of the map 10% of screen
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                width,
                height,
                padding
            )
        )

    }


    //location services


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val coordinate = LatLng(location.latitude, location.longitude)
                        //  userMaker= googleMap!!.addMarker(MarkerOptions().position(coordinate).title("ME"))
                        userMaker.position = coordinate
                        updateCamera()

                        Timber.d("LOCATION-LONG-LAST  %s", "${location.longitude}")
                        Timber.d("LOCATION-LAT-LAST %s", "${location.latitude}")


                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                // val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                //startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID_COARSE_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Timber.d("PERMITIONS  %s", "GRANTED-FRAG")
                    getLastLocation()
                } else {

                    Timber.d("PERMITIONS  %s", "NOT GRANTED")
                }
                return
            }

            else -> {
                Timber.d("PERMITIONS  %s", "IGNORED")
            }
        }
    }


    private fun requestPermissions() {

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID_COARSE_FINE_LOCATION
        )
    }

    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.numUpdates = 5

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            userMaker.position = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            //  Toast.makeText(requireContext(),"lat->${mLastLocation.latitude} lang->${mLastLocation.longitude} ",Toast.LENGTH_LONG).show()
            Timber.d("LOCATION-LONG  %s", "${mLastLocation.longitude}")
            Timber.d("LOCATION-LAT  %s", "${mLastLocation.latitude}")
        }
    }


}
