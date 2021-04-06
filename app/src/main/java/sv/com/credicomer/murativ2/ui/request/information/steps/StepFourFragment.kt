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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

import com.example.creditscomer.viewmodel.UserInfoViewModel

import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentStepFourBinding

class StepFourFragment : Fragment() {

    lateinit var userViewModel:UserInfoViewModel
    lateinit var navController: NavController
    private lateinit var binding: FragmentStepFourBinding
    val REQUEST_IMAGE_CAPTURE = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =  activity.run {
            ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_four, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.location.observe(viewLifecycleOwner, Observer {
            binding.textLocation.text = it
        })

        binding.buttonSendRequest.setOnClickListener {
            userViewModel.changePosition(5)
        }

        var camera_permission = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        var storage_read_permission = ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        var storage_write_permission = ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        binding.buttonPicture.setOnClickListener {
            if (cameraPermissions()){
                Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
                takePictureIntent()
            }
            else{
                Toast.makeText(context, "you should allow pemissions for camera", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context as Activity ,arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
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

    fun cameraPermissions() = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            binding.imageDoc.setImageBitmap(imageBitmap)
        }
    }

}