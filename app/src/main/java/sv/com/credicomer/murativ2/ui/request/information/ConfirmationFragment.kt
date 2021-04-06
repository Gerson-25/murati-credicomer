package com.example.creditscomer.view.information

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.creditscomer.view.information.CalculatorFragmentArgs.Companion.fromBundle
import com.example.creditscomer.viewmodel.UserInfoViewModel
import com.squareup.okhttp.Dispatcher
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import sv.com.credicomer.murativ2.BuildConfig
import sv.com.credicomer.murativ2.FileUtil
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentConfirmationBinding
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfoModel
import sv.com.credicomer.murativ2.utils.edit_text.DateWorker
import sv.com.credicomer.murativ2.utils.edit_text.initField
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class ConfirmationFragment : Fragment() {

    private  lateinit var imageUri: Uri
    private lateinit var energyReceipUri: Uri
    private lateinit var waterReceipUri: Uri
    private lateinit var pdfReceipUri: Uri
    private lateinit var debtFormUri: Uri
    private lateinit var imageDir:String
    private lateinit var  navController: NavController
    private lateinit var viewModel: UserInfoViewModel
    lateinit var binding: FragmentConfirmationBinding
    private val REQUEST_WATER_BILL_CODE = 100
    private val REQUEST_ENERGY_BILL_CODE = 200
    private val REQUEST_FILE_CODE = 300
    private var imageFile:File?=null
    private var compressedImage:File?=null
    private val REQUEST_DEBT_FORM_CODE = 400
    private var documentsList = arrayListOf(
        false, false, false, false
    )

    lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirmation, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val args = ConfirmationFragmentArgs.fromBundle(arguments!!)
        val myInfo = args.myInfo

        navController = Navigation.findNavController(view)
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (!it) {
                Handler().postDelayed({
                    binding.progressBar.visibility = View.GONE
                    binding.animationLoadingSuccess.visibility = View.VISIBLE
                    binding.statusText.text = getString(R.string.succes)
                    Handler().postDelayed({
                        binding.loaderScreen.visibility = View.GONE
                        navController = Navigation.findNavController(view)
                        navController.navigate(ConfirmationFragmentDirections.actionConfirmationFragmentToStartFragment())
                    }, 2000)
                }, 2000)
            }
        })

        viewModel.initUploadCounter()

        binding.buttonAddEnergyBill.setOnClickListener {
            if (cameraPermissions()){
                takePictureIntent(REQUEST_ENERGY_BILL_CODE)
            }
            else{
                Toast.makeText(context, "you should allow pemissions for camera", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context as Activity,arrayOf(Manifest.permission.CAMERA), REQUEST_ENERGY_BILL_CODE)
            }
        }
        binding.buttonAddWaterBill.setOnClickListener {
            if (cameraPermissions()){
                takePictureIntent(REQUEST_WATER_BILL_CODE)
            }
            else{
                Toast.makeText(context, "you should allow pemissions for camera", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context as Activity,arrayOf(Manifest.permission.CAMERA), REQUEST_WATER_BILL_CODE)
            }
        }

        binding.buttonAddDebtForm.setOnClickListener {
            if (cameraPermissions()){
                takePictureIntent(REQUEST_DEBT_FORM_CODE)
            }
            else{
                Toast.makeText(context, "you should allow pemissions for camera", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(context as Activity,arrayOf(Manifest.permission.CAMERA), REQUEST_DEBT_FORM_CODE)
            }
        }

        binding.buttonAddFile.setOnClickListener {
            loadFileIntent(REQUEST_FILE_CODE)
        }

        viewModel.waterUriString.observe(viewLifecycleOwner, Observer {
            myInfo.water_receip = it
        })

        viewModel.energyUriString.observe(viewLifecycleOwner, Observer {
            myInfo.energy_receip = it
        })

        viewModel.pdfUriString.observe(viewLifecycleOwner, Observer {
            myInfo.recommendatio_letter = it
        })

        viewModel.pdfUriString.observe(viewLifecycleOwner, Observer {
            myInfo.recommendatio_letter = it
        })

        viewModel.debtFormUriString.observe(viewLifecycleOwner, Observer {
            myInfo.formato_deuda = it
        })



        viewModel.uploadFiles.observe(viewLifecycleOwner, Observer {
            if (it==4){
                viewModel.sendInfo(myInfo)
            }
        })

        binding.btnSendInfo.setOnClickListener {
            val day = DateWorker.getDate(0,1).dayOfMonth.toInt() - 1

            val monthNo = DateWorker.getDate(0,1).month.toInt() + 1

            val month = if(monthNo < 10 )  "0$monthNo" else monthNo.toString()

            val year = DateWorker.getDate(0,1).year

            myInfo.fecha_solicitud = "$day-$month-$year"

            if (documentsList.contains(false)){
                Toast.makeText(context, "Sube todos los archivos solicitados", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.loaderScreen.visibility = View.VISIBLE
                binding.infoContainer.visibility = View.GONE
                viewModel.saveEnergyReceip(energyReceipUri)
                viewModel.saveWaterReceip(waterReceipUri)
                viewModel.saveFile(pdfReceipUri)
                viewModel.saveDebtForm(debtFormUri)
            }
        }
    }

    private fun loadImage(linearLayout: FrameLayout, textView: TextView){
            linearLayout.background = resources.getDrawable(R.drawable.btn_background_blue)
            textView.visibility = View.VISIBLE

    }

    private fun loadDoc(linearLayout: FrameLayout, textView: TextView){
        linearLayout.background = resources.getDrawable(R.drawable.btn_background_blue)
        textView.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_ENERGY_BILL_CODE -> {
                    energyReceipUri = imageUri
                    imageFile= energyReceipUri.let { FileUtil.from(requireContext(), it) }
                    compressImage()
                    loadImage(binding.imageAddEnergyBill, binding.textAddEnergyBill)
                    binding.textAddEnergyBill.text = imageUri.toString()
                    documentsList[0] = true
                }
                REQUEST_WATER_BILL_CODE -> {
                    waterReceipUri = imageUri
                    imageFile = waterReceipUri.let { FileUtil.from(requireContext(), it) }
                    compressImage()
                    loadImage(binding.imageAddWaterBill, binding.textAddWaterBill)
                    binding.textAddWaterBill.text = imageUri.toString()
                    documentsList[1] = true
                }
                REQUEST_FILE_CODE -> {
                    pdfReceipUri = data!!.data!!
                    loadDoc(binding.imageAddFile, binding.textAddFile)
                    binding.textAddFile.text = pdfReceipUri.toString()
                    documentsList[2] = true
                }
                REQUEST_DEBT_FORM_CODE -> {
                    debtFormUri = imageUri
                    imageFile = debtFormUri.let { FileUtil.from(requireContext(), it) }
                    compressImage()
                    loadImage(binding.imageAddDebtForm, binding.textAddDebtForm)
                    binding.textAddDebtForm.text = debtFormUri.toString()
                    documentsList[3] = true
                }
            }
        }
    }

    private fun takePictureIntent(code: Int){
        var photoFile: File? = null
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {intent->
            intent.resolveActivity(context!!.packageManager)?.also { pictureIntent ->
                try {
                    photoFile = createCapturedPhoto()
                } catch (ex: IOException) {
                }

                if (photoFile != null) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "receip")
                    values.put(
                        MediaStore.Images.Media.DESCRIPTION,
                        "Photo taken on ${System.currentTimeMillis()}"
                    )

                    imageUri = activity!!.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                    )!!

                    val intentCamera = intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                    startActivityForResult(intentCamera, code)
                    imageDir = imageUri.toString()
                }

                //startActivityForResult(intent, code)
            }
        }
    }

    private fun loadFileIntent(code: Int){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, code)
    }

    private fun compressImage() {
        imageFile?.let { imageFile ->
            lifecycleScope.launch {
                compressedImage = Compressor.compress(requireContext(), imageFile)
                Timber.d("RESULT %s", String.format("Size : %s", getReadableFileSize(compressedImage!!.length())))
            }
        } ?: showError("Please choose an image!")
    }

    private fun showError( errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    private fun cameraPermissions() = ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createCapturedPhoto(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timestamp _"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        currentPhotoPath = image.absolutePath
        return image
    }
}