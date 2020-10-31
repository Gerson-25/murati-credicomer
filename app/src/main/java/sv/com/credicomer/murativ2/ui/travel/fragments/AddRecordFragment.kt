@file:Suppress("SameParameterValue")

package sv.com.credicomer.murativ2.ui.travel.fragments


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import butterknife.Unbinder
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.bumptech.glide.Glide
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import sv.com.credicomer.murativ2.FileUtil
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.constants.MAKE_TO_PHOTO
import sv.com.credicomer.murativ2.constants.UPDATE_PHOTO
import sv.com.credicomer.murativ2.databinding.FragmentAddRecordBinding
import sv.com.credicomer.murativ2.notifications.IUploadContentService.RecordEvent
import sv.com.credicomer.murativ2.notifications.UploadContentService
import sv.com.credicomer.murativ2.ui.travel.models.Record
import sv.com.credicomer.murativ2.ui.travel.viewModel.AddRecordViewModel
import sv.com.credicomer.murativ2.utils.edit_text.EditTextUtils
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow


class AddRecordFragment : Fragment() {

    private lateinit var addRecordViewModel: AddRecordViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAddRecordBinding

    private var currentAnimator: Animator? = null

    private var shortAnimationDuration: Int = 0


    var addRecordMainContainer: View? = null

    private val buttonFoodId: Int = 0
    private val buttonHotelId: Int = 2
    private val buttonTransportationId: Int = 1
    private val buttonOtherId: Int = 3

    var radioId: String? = null


    lateinit var record: Record
    lateinit var recordId: String
    lateinit var travelId: String




    private val SELECT_IMAGE: Int = 23748
    private val TAKE_PICTURE: Int = 5000
    private var mCurrentPhotoPath: String? = null
    private var photoUri: Uri? = null
    private var imageDir: String? = null
    private var imageFile:File?=null
    private var compressedImage:File?=null

    private var unbinder: Unbinder? = null


    var mDateStart: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_record, container, false)
        addRecordViewModel = ViewModelProvider(this).get(AddRecordViewModel::class.java)
        navController = findNavController()

        binding.radioButtonFood.id = buttonFoodId
        binding.radioButtonHotel.id = buttonHotelId
        binding.radioButtonTransportation.id = buttonTransportationId
        binding.radioButtonOther.id = buttonOtherId

        val args = arguments?.let { AddRecordFragmentArgs.fromBundle(it) }

        travelId = args?.idTravel!!
        record = args.record

        recordId = record.recordId.toString()

        if (recordId == "") {
            createRecord()
        } else {
            getInitialData(record, travelId)
        }

        // DatePicker
        view?.let {
            ButterKnife.bind(this, it)
            unbinder = ButterKnife.bind(this, it)
        }

        binding.textviewRecordDateSelection.setOnClickListener {
            openDateRangePicker()
        }

        binding.etMonto.filters = EditTextUtils.getDecimalNumber()



        return binding.root

    }

    private fun showResultAnimation(result: Int) {




        addRecordMainContainer?.visibility = View.VISIBLE

        when (result) {

            // Animacion Infinita mientras el proceso de Firebase sigue en ejecucion
            1 -> binding.animationLoadingInfinite.visibility = View.VISIBLE


            // Animacion de fallo cuando el proceso retorna un error
            2 -> binding.animationLoadingFailure.visibility = View.VISIBLE


            // Animacion de exito cuando el proceso ha sido completado sin errores
            3 -> binding.animationLoadingSuccess.visibility = View.VISIBLE

        }
    }


    private fun createRecord() {

        binding.radioGroupCategory.setOnCheckedChangeListener { group, checkedId ->

            val radioButtonSelectedId: Int = binding.radioGroupCategory.checkedRadioButtonId
            radioButtonSelection(radioButtonSelectedId)

            radioId = radioButtonSelectedId.toString()
        }


        radioId = binding.radioGroupCategory.checkedRadioButtonId.toString()

        binding.btnTomarFoto.setOnClickListener {



            if (permissionValidation()) {

                binding.btnTomarFoto.isEnabled
                dialogPhoto()

            } else {
                binding.btnTomarFoto.isEnabled
            }
        }

        binding.btnAgregarRegistro.setOnClickListener {




            when {
                binding.etTituloDeRegistro.text!!.isBlank()
                        or binding.textviewRecordDateSelection.text!!.isBlank()
                        or binding.etMonto.text!!.isBlank()
                        or radioId.equals("-1") -> {
                    Toast.makeText(
                        this.context,
                        "Todos los campos son obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()


                }
                (radioId!! == "3") and binding.editTextRecordDescription.text!!.isBlank() -> {

                    Toast.makeText(this.context, "Debe incluir una descripcion", Toast.LENGTH_SHORT)
                        .show()

                }
                imageDir.equals(null) -> {

                    Toast.makeText(
                        this.context,
                        "Debe seleccionar una fotografia",
                        Toast.LENGTH_SHORT
                    )
                        .show()


                }
                else -> {


                    // Elementos de UI
                    val recordId = ""
                    val recordName = binding.etTituloDeRegistro.text.toString()
                    val recordDate =
                        binding.textviewRecordDateSelection.text.toString()
                    val recordAmount = binding.etMonto.text.toString()
                    val recordCategory =
                        binding.radioGroupCategory.checkedRadioButtonId.toString()
                    val recordPhoto = ""
                    val recordDescription: String =
                        binding.editTextRecordDescription.text.toString()
                    val recordDateRegistered = getDateTime()
                    val recordDateLastUpdate = ""


                    val addNewRecord = Record(
                        recordId,
                        recordName,
                        recordDate,
                        String.format("%.2f", recordAmount.toDouble()),
                        recordCategory,
                        recordPhoto,
                        recordDescription,
                        recordDateRegistered,
                        recordDateLastUpdate
                    )

                    uploadRecord(addNewRecord, RecordEvent.NEW_RECORD)

                    navController.popBackStack()


                }
            }
        }
    }


    private fun getInitialData(records: Record, travelId: String) {


        binding.radioGroupCategory.setOnCheckedChangeListener { _, _ ->

            val radioButtonSelectedId: Int = binding.radioGroupCategory.checkedRadioButtonId
            radioButtonSelection(radioButtonSelectedId)

            radioId = radioButtonSelectedId.toString()
        }


        binding.etTituloDeRegistro.setText(records.recordName)
        binding.textviewRecordDateSelection.text = records.recordDate
        binding.etMonto.setText(records.recordMount)
        binding.radioGroupCategory.check(records.recordCategory!!.toInt())
        binding.editTextRecordDescription.setText(records.recordDescription)
        radioId = records.recordCategory



        Glide.with(this).load(records.recordPhoto).into(binding.ivImageGallery)

        val initialImageDirFromFirebase = records.recordPhoto
        imageDir = records.recordPhoto



        binding.btnTomarFoto.text = MAKE_TO_PHOTO
        binding.btnAgregarRegistro.text = UPDATE_PHOTO




        binding.btnTomarFoto.setOnClickListener {



            if (permissionValidation()) {

                binding.btnTomarFoto.isEnabled
                dialogPhoto()

            } else {
                binding.btnTomarFoto.isEnabled
            }
        }



        binding.btnAgregarRegistro.setOnClickListener {



            when {
                binding.etTituloDeRegistro.text.isBlank()
                        or binding.textviewRecordDateSelection.text.isBlank()
                        or binding.etMonto.text.isBlank()
                        or radioId.equals("-1") -> {
                    Toast.makeText(
                        this.context,
                        "Todos los campos son obligatorios",
                        Toast.LENGTH_SHORT
                    ).show()


                }
                (radioId!! == "3") and binding.btnAgregarRegistro.text.isBlank() -> {

                    Toast.makeText(this.context, "Debe incluir una descripcion", Toast.LENGTH_SHORT)
                        .show()

                }
                imageDir.equals(initialImageDirFromFirebase) -> {

                    val recordName: String = binding.etTituloDeRegistro.text.toString()
                    val recordDate: String = binding.textviewRecordDateSelection.text.toString()
                    val recordAmount: String = binding.etMonto.text.toString()
                    val recordCategory: String =
                        binding.radioGroupCategory.checkedRadioButtonId.toString()
                    val recordDescription: String =
                        binding.editTextRecordDescription.text.toString()
                    val recordDateLastUpdate: String = getDateTime()


                    record.apply {
                        this.recordName=recordName
                        this.recordDate=recordDate
                        this.recordCategory=recordCategory
                        this.recordMount=String.format("%.2f", recordAmount.toDouble())
                        this.recordDescription=recordDescription
                        this.recordUpdateRegister=recordDateLastUpdate
                    }/*
                    val record = Record(

                        recordName,
                        recordDate,
                        String.format("%.2f", recordAmount.toDouble()),
                        recordCategory,
                        recordDescription,
                        recordDateLastUpdate
                    )*/
/*
                    addRecordViewModel.updateRecord(
                        record,
                        travelId,
                        records.recordId.toString(),
                        requireContext()
                    )*/

                    uploadRecord(record, RecordEvent.UPDATE_RECORD_NO_PHOTO)
                    navController.navigate(AddRecordFragmentDirections.actionAddRecordFragmentToNavHomeTravel(travelId,"",true))

                }
                else -> {

                            val recordName: String = binding.etTituloDeRegistro.text.toString()
                            val recordDate: String =
                                binding.textviewRecordDateSelection.text.toString()
                            val recordAmount: String = binding.etMonto.text.toString()
                            val recordCategory: String =
                                binding.radioGroupCategory.checkedRadioButtonId.toString()
                            val recordDescription: String =
                                binding.editTextRecordDescription.text.toString()
                            val recordDateLastUpdate: String? = getDateTime()


                    record.apply {
                        this.recordName=recordName
                        this.recordDate=recordDate
                        this.recordCategory=recordCategory
                        this.recordMount=String.format("%.2f", recordAmount.toDouble())
                        this.recordDescription=recordDescription
                        this.recordUpdateRegister=recordDateLastUpdate
                    }
                          /*  val recordsFS = Record(
                                recordName,
                                recordDate,
                                recordAmount,
                                recordCategory,
                                recordPhoto,
                                recordDescription,
                                recordDateLastUpdate!!
                            )*/
                            // Envio de Datos

                        uploadRecord(record,RecordEvent.UPDATE_RECORD_NEW_PHOTO)
                           /* addRecordViewModel.updatePhoto(
                                travelId,
                                recordsFS,
                                records.recordId.toString(),
                                requireContext()
                            )*/

                    navController.navigate(AddRecordFragmentDirections.actionAddRecordFragmentToNavHomeTravel(travelId,"",true))


                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            sdf.format(Calendar.getInstance().time)
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun openDateRangePicker() { //Metodo para abrir el calendario
        val pickerFrag = DatePickerFragment()
        pickerFrag.setCallback(object : DatePickerFragment.Callback {
            override fun onCancelled() {
                Toast.makeText(
                    activity,
                    getString(R.string.User_cancel_datapicker),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDateTimeRecurrenceSet(
                selectedDate: SelectedDate, hourOfDay: Int, minute: Int,
                recurrenceOption: SublimeRecurrencePicker.RecurrenceOption,
                recurrenceRule: String?
            ) {
                @SuppressLint("SimpleDateFormat")
                val formatDate = SimpleDateFormat("dd-MM-yyyy")
                mDateStart = formatDate.format(selectedDate.startDate.time)

                binding.textviewRecordDateSelection.text = mDateStart

            }
        })

        val options = SublimeOptions()
        options.setCanPickDateRange(true)
        options.pickerToShow = SublimeOptions.Picker.DATE_PICKER
        val bundle = Bundle()
        bundle.putParcelable("SUBLIME_OPTIONS", options)
        pickerFrag.arguments = bundle
        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        pickerFrag.show(activity!!.supportFragmentManager, "SUBLIME_PICKER")
    }


    private fun radioButtonSelection(radioButtonId: Int) {

        when (radioButtonId) {
            binding.radioButtonFood.id -> {

                binding.radioButtonFood.setBackgroundResource(R.drawable.ic_cat_food_gradient_on)
                binding.radioButtonHotel.setBackgroundResource(R.drawable.ic_cat_hotel_gradient_off)
                binding.radioButtonTransportation.setBackgroundResource(R.drawable.ic_cat_transportation_gradient_off)
                binding.radioButtonOther.setBackgroundResource(R.drawable.ic_cat_other_gradient_off)

            }
            binding.radioButtonHotel.id -> {

                binding.radioButtonFood.setBackgroundResource(R.drawable.ic_cat_food_gradient_off)
                binding.radioButtonHotel.setBackgroundResource(R.drawable.ic_cat_hotel_gradient_on)
                binding.radioButtonTransportation.setBackgroundResource(R.drawable.ic_cat_transportation_gradient_off)
                binding.radioButtonOther.setBackgroundResource(R.drawable.ic_cat_other_gradient_off)

            }
            binding.radioButtonTransportation.id -> {

                binding.radioButtonFood.setBackgroundResource(R.drawable.ic_cat_food_gradient_off)
                binding.radioButtonHotel.setBackgroundResource(R.drawable.ic_cat_hotel_gradient_off)
                binding.radioButtonTransportation.setBackgroundResource(R.drawable.ic_cat_transportation_gradient_on)
                binding.radioButtonOther.setBackgroundResource(R.drawable.ic_cat_other_gradient_off)

            }
            binding.radioButtonOther.id -> {

                binding.radioButtonFood.setBackgroundResource(R.drawable.ic_cat_food_gradient_off)
                binding.radioButtonHotel.setBackgroundResource(R.drawable.ic_cat_hotel_gradient_off)
                binding.radioButtonTransportation.setBackgroundResource(R.drawable.ic_cat_transportation_gradient_off)
                binding.radioButtonOther.setBackgroundResource(R.drawable.ic_cat_other_gradient_on)
            }
        }
    }



    //Permisos de camara, lectura y escritura
    private fun permissionValidation(): Boolean {
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
        dialogPermission.setPositiveButton(
            "Aceptar"
        ) { _: DialogInterface, _: Int ->
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
                binding.btnTomarFoto.isEnabled
            }
        }
    }
    //Fin de permisos


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
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
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

                            val intentCamera = cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                            startActivityForResult(intentCamera, TAKE_PICTURE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {

            if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
                val selectedImage: Uri? = data.data
                imageDir = selectedImage.toString()
                Timber.d("URIPHOTO1 %s", imageDir)
                imageFile= selectedImage?.let { FileUtil.from(requireContext(), it) }
                compressImage()
                binding.ivImageGallery.apply {

                    setImageURI(selectedImage)
                    setOnClickListener {view->
                        selectedImage?.let { uri -> zoomImageFromThumb(view, uri) }
                        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
                    }
                }

            }
            if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
                val selectedImage: Uri? = photoUri
                imageFile= selectedImage?.let { FileUtil.from(requireContext(), it) }
                compressImage()
                binding.ivImageGallery.apply {
                    setImageURI(selectedImage)
                    setOnClickListener { view ->
                        selectedImage?.let { uri -> zoomImageFromThumb(view, uri) }
                        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
                    }

                }
            }
        } catch (e: java.lang.Exception) {

        }
    }


    private fun zoomImageFromThumb(thumbView: View, uri: Uri) {

        currentAnimator?.cancel()


        binding.expandedImage.setImageURI(uri)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()


        thumbView.getGlobalVisibleRect(startBoundsInt)
        binding.container
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        binding.container.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)


        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {

            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {

            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }


        thumbView.alpha = 0f
        binding.expandedImage.apply {

            visibility = View.VISIBLE
            pivotX = 0f
            pivotY = 0f

        }


        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    binding.expandedImage,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        binding.expandedImage,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }


        binding.expandedImage.apply {

            setOnClickListener {
                currentAnimator?.cancel()


                currentAnimator = AnimatorSet().apply {
                    play(ObjectAnimator.ofFloat(binding.expandedImage, View.X, startBounds.left)).apply {
                        with(ObjectAnimator.ofFloat(binding.expandedImage, View.Y, startBounds.top))
                        with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_X, startScale))
                        with(ObjectAnimator.ofFloat(binding.expandedImage, View.SCALE_Y, startScale))
                    }
                    duration = shortAnimationDuration.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {

                        override fun onAnimationEnd(animation: Animator) {
                            thumbView.alpha = 1f
                            visibility = View.GONE
                            currentAnimator = null
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            thumbView.alpha = 1f
                           visibility = View.GONE
                            currentAnimator = null
                        }
                    })
                    start()
                }
            }

        }


    }

    @Suppress("SameParameterValue")
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

    private fun uploadRecord(record: Record,event: RecordEvent){
        Intent(requireContext(), UploadContentService::class.java).also { intent ->
            if (event==RecordEvent.UPDATE_RECORD_NEW_PHOTO || event== RecordEvent.NEW_RECORD) {
                compressedImage?.let { photo -> intent.putExtra("photo", photo.toUri().toString()) }
            }
            intent.putExtra("record", record)
            intent.putExtra("travelId", travelId)
            intent.putExtra("event", event)
            activity?.startService(intent)
        }
    }
    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

}
