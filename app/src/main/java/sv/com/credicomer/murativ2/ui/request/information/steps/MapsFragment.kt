package sv.com.credicomer.murativ2.ui.request.information.steps

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.creditscomer.viewmodel.UserInfoViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentMapsBinding

class MapsFragment : Fragment() {

    val FRAGMENT_KEY = "getResult"

    lateinit var userViewModel:UserInfoViewModel

    private var googleMap2: GoogleMap? = null

    lateinit var binding: FragmentMapsBinding

    private lateinit var latLng: LatLng

    private val AUTOCOMPLETE_REQUEST = 1
    var focusSwitcher = 1

    private lateinit var placesClient: PlacesClient

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap2 = googleMap
        val sanSalvador = LatLng(13.702503, -89.205748)
        googleMap.uiSettings.isZoomControlsEnabled = true

        googleMap.addMarker(MarkerOptions().position(sanSalvador).title("San Salvador"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sanSalvador))

        val builder = LatLngBounds.Builder()
        builder.include(sanSalvador)

        val cameraPosition = CameraPosition.builder()
            .target(sanSalvador)
            .zoom(16F)
            .build()

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            googleMap.animateCamera(cameraUpdate)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapsView.onCreate(savedInstanceState)
        binding.mapsView.getMapAsync(callback)

        binding.textSearchLocation.setOnFocusChangeListener { view, b ->
            if (focusSwitcher == 1){
                startAutoCompleteActivity()
                focusSwitcher = 0
            }
        }

        initPlaces()

    }

    private fun initPlaces(){
        Places.initialize(context!!, resources.getString(R.string.google_maps_key))
        placesClient = Places.createClient(context!!)
    }
    private fun startAutoCompleteActivity(){
        val placesIntent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.OVERLAY, arrayListOf(
            Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS
        )).build(context!!)
        startActivityForResult(placesIntent, AUTOCOMPLETE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.textSearchLocation.clearFocus()
        focusSwitcher = 1
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  AUTOCOMPLETE_REQUEST){
            if (resultCode == RESULT_OK){
                val place = Autocomplete.getPlaceFromIntent(data!!)

                latLng = place.latLng!!
                val placeName = place.name
                binding.textViewLocation.text = place.address
                updateCamera(latLng = latLng!!, address = placeName!!)
                binding.textSearchLocation.setText(placeName)
                binding.confirmLocationContainer.visibility = View.VISIBLE
                userViewModel.saveLocation(place.address!!)
            }
            else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                val status = Autocomplete.getStatusFromIntent(data!!)
            }
            else if(resultCode == RESULT_CANCELED){

            }
        }
    }

    private fun updateCamera(latLng: LatLng, address:String){

        var newLatLng = latLng
        val builder = LatLngBounds.Builder()
        builder.include(newLatLng)

        googleMap2!!.addMarker(MarkerOptions().position(newLatLng).title(address))

        var cameraPosition = CameraPosition.builder()
            .target(newLatLng)
            .zoom(16F)
            .build()

        var cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        googleMap2!!.animateCamera(cameraUpdate)
    }

    override fun onResume() {
        super.onResume()
        binding.mapsView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapsView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapsView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapsView.onSaveInstanceState(outState)
    }

}