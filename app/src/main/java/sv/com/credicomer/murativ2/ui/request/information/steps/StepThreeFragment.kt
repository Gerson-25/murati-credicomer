package sv.com.credicomer.murativ2.ui.request.information.steps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.creditscomer.viewmodel.UserInfoViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStepThreeBinding
import sv.com.credicomer.murativ2.ui.request.information.SlideInfoFragmentDirections
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfoModel
import sv.com.credicomer.murativ2.utils.HOME_TYPES
import sv.com.credicomer.murativ2.utils.departamentos

class StepThreeFragment : Fragment() {
    lateinit var navController: NavController
    val REQUEST_IMAGE_CAPTURE = 200
    lateinit var userViewModel:UserInfoViewModel
    private lateinit var binding: FragmentStepThreeBinding
    private val AUTOCOMPLETE_REQUEST = 1
    private lateinit var placesClient: PlacesClient
    private lateinit var latLng: LatLng
    var focusSwitcher = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_three, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSpinners()
        initPlaces()
        binding.edittextAddress.setOnFocusChangeListener { view, b ->
            if (focusSwitcher == 1){
                startAutoCompleteActivity()
                focusSwitcher = 0
            }
        }
    }

    fun initSpinners(){

        val dep = departamentos.keys.toList()

        ArrayAdapter(activity!!, R.layout.custom_spinner_layout, HOME_TYPES)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerHomeType.apply {
                    adapter = it
                }
            }

        binding.buttonPicture.setOnClickListener {
            if (cameraPermissions()){
                Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
                takePictureIntent()
            }
            else{
                Toast.makeText(context, "you should allow pemissions for camera", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context as Activity,arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
            }
        }
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
        binding.edittextAddress.clearFocus()
        focusSwitcher = 1
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            binding.imageviewCheck.visibility = View.VISIBLE
        }
        else if (requestCode ==  AUTOCOMPLETE_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                var place = Autocomplete.getPlaceFromIntent(data!!)
                latLng = place.latLng!!
                var placeName = place.name
                //binding.textViewLocation.text = place.address
                //updateCamera(latLng = latLng!!, address = placeName!!)
                binding.edittextAddress.setText(place.address)
                //binding.confirmLocationContainer.visibility = View.VISIBLE
                userViewModel.saveLocation(place.address!!)
            }
            else if(resultCode == AutocompleteActivity.RESULT_ERROR){
                val status = Autocomplete.getStatusFromIntent(data!!)
            }
            else if(resultCode == Activity.RESULT_CANCELED){
            }
        }
    }

    fun takePictureIntent(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
            it.resolveActivity(context!!.packageManager)?.also { pictureIntent ->
                startActivityForResult(it, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun saveData(department:String, city:String, occupation:String ){
        val generalInfo =
            hashMapOf(
                "sport" to department,
                "hobbie" to city,
                "house" to occupation
            )

        //userViewModel.saveGeneralInfo(generalInfo)
    }

    fun cameraPermissions() = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}