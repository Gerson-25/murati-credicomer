package sv.com.credicomer.murati.ui.alliance.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murati.MainViewModel
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.CreateCarnetFragmentBinding
import sv.com.credicomer.murati.ui.alliance.models.UserCarnet
import sv.com.credicomer.murati.ui.alliance.viewModels.CreateCarnetViewModel
import sv.com.credicomer.murati.ui.ride.getToken
import sv.com.credicomer.murati.utils.edit_text.EditTextUtils
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateCarnetFragment : Fragment() {

    private lateinit var viewModel: CreateCarnetViewModel
    private lateinit var navController: NavController
    private lateinit var binding: CreateCarnetFragmentBinding
    private val user = FirebaseAuth.getInstance().currentUser

    //Fotografia
    private val SELECT_IMAGE: Int = 23748
    private val TAKE_PICTURE: Int = 5000
    private var mCurrentPhotoPath: String? = null
    private var photoUri: Uri? = null
    private var imageDir: String? = null

    // Contenedores / Vistas XML
    private var addRecordMainContainer: View? = null
    private var pathImage: TextView? = null

    private lateinit var collectionPath: String
    private lateinit var subCollectionPath: String
    private lateinit var mainViewModel: MainViewModel

    var showCarnet:Int?=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
        navController = findNavController()
        collectionPath = mainViewModel.allianceCollectionPath.value.toString()
        subCollectionPath = mainViewModel.allianceSubCollectionPath.value.toString()
        viewModel = ViewModelProvider(this).get(CreateCarnetViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.create_carnet_fragment, container, false)
        viewModel.getCarnet()
        val args = CreateCarnetFragmentArgs.fromBundle(arguments!!)
        showCarnet = args.settings

        binding.editTextName.filters = EditTextUtils.getOnlyLetter()

        when(showCarnet){
            0->{
                viewModel.isCarnet.observe(viewLifecycleOwner, Observer {
                    it.let {
                        if (it.isNotEmpty()) {
                            if (collectionPath.contains("unicomer")) {
                                binding.carnet = it[0]
                                Glide.with(this)
                                    .load(it[0].carnetPhoto)
                                    .centerCrop()
                                    .into(binding.imageView24)
                                binding.frameCarnet.visibility = View.VISIBLE
                            } else {
                                binding.carnet = it[0]
                                Glide.with(this).load(it[0].carnetPhoto).into(binding.imagePhotoCredicomer)
                                binding.frameCarnetCredicomer.visibility = View.VISIBLE

                            }

                        } else {

                            if (collectionPath.contains("unicomer")) {
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                            } else {
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                                binding.titleDepartmentName.text = "Cargo* :"
                            }

                            binding.btnTomarFotoCarnet.setOnClickListener {
                                // Pedir los permisos para abrir la camara

                                if (permissionValidation()) {

                                    binding.btnTomarFotoCarnet.isEnabled
                                    dialogPhoto()

                                } else {
                                    binding.btnTomarFotoCarnet.isEnabled
                                }
                            }
                            binding.btnAgregarCarnet.setOnClickListener {
                                createCarnet()
                            }
                        }
                    }

                })
            }
            1->{
                viewModel.isCarnet.observe(viewLifecycleOwner, Observer {
                    it.let {
                        if (it.isNotEmpty()) {
                            if (collectionPath.contains("unicomer")) {
                                binding.carnet = it[0]
                                Glide.with(this)
                                    .load(it[0].carnetPhoto)
                                    .centerCrop()
                                    .into(binding.imageView24)
                                binding.frameCarnet.visibility = View.VISIBLE
                            } else {
                                binding.carnet = it[0]
                                Glide.with(this).load(it[0].carnetPhoto).into(binding.imagePhotoCredicomer)
                                binding.frameCarnetCredicomer.visibility = View.VISIBLE

                            }

                        } else {

                            Toast.makeText(requireContext(), "No ha creado su carnet aun",Toast.LENGTH_LONG).show()
                        }
                    }

                })
            }
            2->{

                viewModel.isCarnet.observe(viewLifecycleOwner, Observer {
                    it.let {
                        if (it.isNotEmpty()) {

                            if (collectionPath.contains("unicomer")) {
                                getCarnet(it[0])
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                            } else {
                                getCarnet(it[0])
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                                binding.titleDepartmentName.text = "Cargo* :"
                            }

                        }else {

                            if (collectionPath.contains("unicomer")) {
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                            } else {
                                binding.frameCreateCarnet.visibility = View.VISIBLE
                                binding.titleDepartmentName.text = "Cargo* :"
                            }

                            binding.btnTomarFotoCarnet.setOnClickListener {
                                // Pedir los permisos para abrir la camara

                                if (permissionValidation()) {

                                    binding.btnTomarFotoCarnet.isEnabled
                                    dialogPhoto()

                                } else {
                                    binding.btnTomarFotoCarnet.isEnabled
                                }
                            }
                            binding.btnAgregarCarnet.setOnClickListener {
                                createCarnet()
                            }
                        }
                    }

                })
            }
        }



        return binding.root
    }

    private fun createCarnet() {
        if (binding.editTextName.text.toString().isEmpty()
            || binding.editTextCollaboratorCod.text.toString().isEmpty()
            || binding.editTextDepartmentName.text.toString().isEmpty()
            || imageDir.equals(null)
        ) {
            Toast.makeText(activity, activity!!.getString(R.string.error_hint), Toast.LENGTH_SHORT)
                .show()
        } else {

            // Aqui se carga el XML con la animacion infinita
            showResultAnimation(1)
            imageDir?.let { it1 -> viewModel.getPhoto(it1, "") }
            viewModel.imRecord.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                // Elementos de UI
                val email = user?.email
                val name = binding.editTextName.text.toString()
                val collaboratorCod = binding.editTextCollaboratorCod.text.toString()
                val departmentName = binding.editTextDepartmentName.text.toString()
                val carnetPhoto = it
                val token = getToken()

                val carnet = UserCarnet(
                    email, name, collaboratorCod, departmentName, "$carnetPhoto", token
                )
                viewModel.createCarnet(carnet)
                viewModel.anim.observe(viewLifecycleOwner, Observer { anim ->
                    if (anim) {
                        showResultAnimation(3)
                        Handler().postDelayed({
                            navController.navigate(CreateCarnetFragmentDirections.actionCreateCarnetFragmentToNavAlliance())
                        }, 3000)

                    } else {
                        showResultAnimation(2)
                    }
                })
            })
        }
    }

    private fun getCarnet(userCarnet: UserCarnet){
        binding.editTextName.setText(userCarnet.name)
        binding.editTextDepartmentName.setText(userCarnet.departmentName)
        binding.editTextCollaboratorCod.setText(userCarnet.collaboratorCod)
        val initialImageDirFromFirebase = userCarnet.carnetPhoto
        imageDir = userCarnet.carnetPhoto



        binding.btnTomarFotoCarnet.setText("MODIFICAR MI FOTOGRAFIA")
        binding.btnAgregarCarnet.setText("ACTUALIZAR CARNET VIRTUAL")

        //Modificando datos

        binding.btnTomarFotoCarnet.setOnClickListener {

            if (permissionValidation()) {

                binding.btnTomarFotoCarnet.isEnabled
                dialogPhoto()

            } else {
                binding.btnTomarFotoCarnet.isEnabled
            }


        }

        binding.btnAgregarCarnet.setOnClickListener {
            // Comprobar nuevamente que los campos no esten vacios o solamente sean espacios

            if (binding.editTextName.text.toString().isEmpty()
                || binding.editTextCollaboratorCod.text.toString().isEmpty()
                || binding.editTextDepartmentName.text.toString().isEmpty()
                || imageDir.equals(null)
            ) {
                Toast.makeText(activity, activity!!.getString(R.string.error_hint), Toast.LENGTH_SHORT)
                    .show()
            }else if(imageDir.equals(initialImageDirFromFirebase)){
                showResultAnimation(1)
                // Elementos de UI
                val name = binding.editTextName.text.toString()
                val collaboratorCod = binding.editTextCollaboratorCod.text.toString()
                val departmentName = binding.editTextDepartmentName.text.toString()

                val carnet = UserCarnet(
                     name, collaboratorCod, departmentName
                )
                viewModel.updateCarnet(carnet)
                viewModel.anim.observe(viewLifecycleOwner, Observer { anim ->
                    if (anim) {
                        showResultAnimation(3)
                        Handler().postDelayed({
                            navController.navigate(CreateCarnetFragmentDirections.actionCreateCarnetFragmentToNavAlliance())
                        }, 3000)

                    } else {
                        showResultAnimation(2)
                    }
                })
            } else {
                // Si todos los campos estan correctos, actualizar la informacion en Firebase
                // Si el URI de la foto ha sido cambiada entonces se ejecuta este codigo
                // INICIALIZANDO INSTANCIA DE FIREBASE

                showResultAnimation(1)
                imageDir?.let { it1 -> viewModel.getPhoto(it1,
                    initialImageDirFromFirebase.toString()
                ) }

                viewModel.imRecord.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    // Elementos de UI
                    val name = binding.editTextName.text.toString()
                    val collaboratorCod = binding.editTextCollaboratorCod.text.toString()
                    val departmentName = binding.editTextDepartmentName.text.toString()
                    val carnetPhoto = it

                    val carnet = UserCarnet(
                         name, collaboratorCod, departmentName, "$carnetPhoto"
                    )
                    viewModel.updateCarnetPhoto(carnet)
                    viewModel.anim.observe(viewLifecycleOwner, Observer { anim ->
                        if (anim) {
                            showResultAnimation(3)
                            Handler().postDelayed({
                                navController.navigate(CreateCarnetFragmentDirections.actionCreateCarnetFragmentToNavAlliance())
                            }, 3000)

                        } else {
                            showResultAnimation(2)
                        }
                    })
                })


            }

        }
    }

    private fun dialogPhoto() {
        try {
            var photoFile: File? = null
            val items = arrayOf<CharSequence>(
                getString(R.string.gallery), getString(
                    R.string.camera
                )
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.select_image))
            builder.setItems(items) { _: DialogInterface, i: Int ->
                when (i) {
                    0 -> {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        intent.type = "image/*"
                        startActivityForResult(intent, SELECT_IMAGE)
                    }
                    1 -> {
                        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (camaraIntent.resolveActivity(activity!!.packageManager) != null) {
                            try {
                                photoFile = createImageFile()
                            } catch (ex: IOException) {
                            }
                        }
                        if (photoFile != null) {
                            val values = ContentValues()
                            values.put(MediaStore.Images.Media.TITLE, "Ticket")
                            values.put(
                                MediaStore.Images.Media.DESCRIPTION,
                                "Photo taken on ${System.currentTimeMillis()}"
                            )

                            photoUri = activity!!.contentResolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values
                            )
                            camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                            startActivityForResult(camaraIntent, TAKE_PICTURE)
                            imageDir = photoUri.toString()

                        }
                    }
                }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        } catch (e: Exception) {

            Toast.makeText(this.context, "ERROR", Toast.LENGTH_SHORT).show()
        }



    }

    @SuppressLint("SimpleDateFormat")
    fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timestamp _"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    private fun showResultAnimation(resultado: Int) {

        // Aqui se define la visibilidad del XML que contiene las animaciones en AddRecordFragment XML
        // Son 3 estados de animacion

        val visibility: Int = View.VISIBLE

        //addRecordAnimationContainer?.visibility = visibility
        addRecordMainContainer?.visibility != visibility

        when (resultado) {

            // Animacion Infinita mientras el proceso de Firebase sigue en ejecucion
            1 -> binding.animationLoadingInfinite.visibility = visibility


            // Animacion de fallo cuando el proceso retorna un error
            2 -> binding.animationLoadingFailure.visibility = visibility

            // Animacion de exito cuando el proceso ha sido completado sin errores
            3 -> binding.animationLoadingSuccess.visibility = visibility
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            Log.d("TAG","requestCode: $requestCode, resultCode: $resultCode, activity result: ${Activity.RESULT_OK}" )
            if (requestCode == 5000 && resultCode == Activity.RESULT_OK) {
                /*val selectedImage: Uri = data?.data!!
                pathImage!!.text = selectedImage.toString()*/
                binding.addedTextview.visibility = View.VISIBLE
            }
            if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                val selectedImage: Uri = data.data!!
                imageDir = selectedImage.toString()
                binding.addedTextview.visibility = View.VISIBLE

            }
    }

    //Permisos de camara, lectura y escritura
    private fun permissionValidation(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if ((ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return true
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.CAMERA
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            permissionDialog()

        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100
            )
        }

        return false
    }

    private fun permissionDialog() {
        val dialogPermission: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogPermission.setTitle("Permisos Desactivados")
        dialogPermission.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app")
        dialogPermission.setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), 100
            )
        }
        dialogPermission.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.size == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                binding.btnTomarFotoCarnet.isEnabled
            }
        }
    }
    //Fin de permisos


}
