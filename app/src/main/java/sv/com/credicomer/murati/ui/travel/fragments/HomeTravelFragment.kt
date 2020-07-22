package sv.com.credicomer.murati.ui.travel.fragments

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.opencsv.CSVWriter
import sv.com.credicomer.murati.MainViewModel
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentHomeTravelBinding
import sv.com.credicomer.murati.ui.travel.adapter.AdapterHTItem
import sv.com.credicomer.murati.ui.travel.adapter.HomeTravelAdapter
import sv.com.credicomer.murati.ui.travel.db
import sv.com.credicomer.murati.ui.travel.finishTravel
import sv.com.credicomer.murati.ui.travel.models.Record
import sv.com.credicomer.murati.ui.travel.viewModel.HomeTravelViewModel
import timber.log.Timber
import java.io.File
import java.io.FileWriter

class HomeTravelFragment : Fragment() {

    private lateinit var homeTravelViewModel: HomeTravelViewModel
    private lateinit var binding: FragmentHomeTravelBinding
    private lateinit var navController: NavController
    private lateinit var idMain: String
    private lateinit var dateMain: String
    private lateinit var idOldTrvel: String
    private lateinit var dateOldTravel: String
    private  var isActual: Boolean = false
    private var isOld: Boolean =false
    private lateinit var vieModelMainViewModel: MainViewModel


    var description: String? = null
    private var balance: String? = null


    private lateinit var dataRecords: Array<String?>

    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var travelRef: CollectionReference = db.collection("e-Tracker")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_travel, container, false)
        navController = findNavController()
        homeTravelViewModel = ViewModelProvider(this).get(HomeTravelViewModel::class.java)
        vieModelMainViewModel =
            activity.run { ViewModelProvider(this!!).get(MainViewModel::class.java) }


        idMain = vieModelMainViewModel.travelId.value.toString()
        dateMain = vieModelMainViewModel.date.value.toString()
        isActual = if(vieModelMainViewModel.isActive.value==null){
            false
        }else{
            vieModelMainViewModel.isActive.value!!
        }



        val args = arguments?.let { HomeTravelFragmentArgs.fromBundle(it) }
        idOldTrvel = args?.id.toString()
        dateOldTravel = args?.date.toString()
        isOld = args?.isTravelActive!!

        Timber.d("ISACTUAL %s"," the value is-> $isActual")
        Timber.d("IDMAIN %s"," the value is-> $idMain")
        Timber.d("IDOLDTRAVEL %s"," the value is-> $idOldTrvel")
        Timber.d("ISOLD %s"," the value is-> $isOld")

        when {
            isActual&&isOld -> {
                showDataHeader(idOldTrvel)
            }
            !isActual && isOld -> {
                showDataHeader(idOldTrvel)
            }
            else -> {
                showDataHeader(idOldTrvel)
                binding.idEditTravelImageView.visibility = View.GONE
                binding.floatingActionButtonHomeTravel.hide()
                binding.floatingActionButtonHTCreateReport.show()
            }
        }




        permissionValidation()

        setHasOptionsMenu(true)



        binding.idEditTravelImageView.setOnClickListener {

            navController.navigate(
                HomeTravelFragmentDirections.actionNavHomeToTravelRegistrationFragment(
                    idOldTrvel,
                    dateMain
                )
            )
        }




        binding.floatingActionButtonHomeTravel.setOnClickListener {
            navController.navigate(
                HomeTravelFragmentDirections.actionNavHomeTravelToAddRecordFragment(
                    idOldTrvel,
                    Record()
                )
            )
        }

        binding.floatingActionButtonHTCreateReport.setOnClickListener {
            val alertDialog = context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Generar reporte")
                    .setMessage("¿Seguro que desea generar su reporte de viaticos?")
                    .setPositiveButton("GENERAR") { _, _ ->
                        //METODO QUE GENERA EL REPORTE
                        email(idOldTrvel, context!!)
                    }
                    .setNegativeButton("CANCELAR") { _, _ ->
                        Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
                    }
            }
            alertDialog!!.show()
        }

        val adapter = HomeTravelAdapter(idOldTrvel)
        binding.recyclerRecord.adapter = adapter
        binding.recyclerRecord.layoutManager = LinearLayoutManager(this.context)


        homeTravelViewModel.records.observe(viewLifecycleOwner, Observer {

            it.let { records->
                val data = records?.map { maped -> AdapterHTItem(maped, 0)}?: emptyList()
                if (data.isEmpty()){ //visibility of empty state
                    binding.backgroundRecyclerView.visibility = View.VISIBLE
                }else{
                    binding.backgroundRecyclerView.visibility = View.GONE
                }
                adapter.submitList(records)
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if(isOld){
            inflater.inflate(R.menu.tracker_menu, menu)
        }
        else inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Manejar seleccion de Item en Menu (Toolbar)

        return when (item.itemId) {

            R.id.nav_home -> {
                // Toast.makeText(requireContext(),"HELLO",Toast.LENGTH_LONG).show()
                navController.navigate(
                    HomeTravelFragmentDirections.actionNavHomeTravelToNavHome2(true)
                )
                true
            }
            R.id.termnsFragment -> {
                //Toast.makeText(requireContext(), "Estas en terminos", Toast.LENGTH_LONG).show()
                navController.navigate(HomeTravelFragmentDirections.actionNavHomeTravelToTermnsFragment2())

                true
            }
            R.id.item_fin_viaje -> {
                val alertDialog = context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Finalizar el viaje")
                        .setMessage("¿Seguro que desea finalizar su viaje? \n Recuerda generar tu reporte antes de finalizarlo!!!")
                        .setPositiveButton("FINALIZAR") { _, _ ->
                            finishTravel(idOldTrvel, context!!)
                            //email(idOldTrvel, context!!)
                            //send to homeTravel
                            navController.navigate(
                                HomeTravelFragmentDirections.actionNavHomeTravelToNavHome(
                                    false
                                )
                            )

                        }
                        .setNegativeButton("CANCELAR") { _, _ ->
                            Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
                        }
                }
                alertDialog!!.show()
                true
            }
            R.id.item_generar -> {
                val alertDialog = context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Generar reporte")
                        .setMessage("¿Seguro que desea generar su reporte de viaticos?")
                        .setPositiveButton("GENERAR") { _, _ ->
                            //METODO QUE GENERA EL REPORTE
                            email(idOldTrvel, context!!)
                        }
                        .setNegativeButton("CANCELAR") { _, _ ->
                            Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
                        }
                }
                alertDialog!!.show()
                true
            }


            else -> {
                NavigationUI.onNavDestinationSelected(item, navController)
                // super.onOptionsItemSelected(item)
            }

        }


    }


    private fun showDataHeader(id: String) {
        homeTravelViewModel.getDataHeader(id)
        homeTravelViewModel.viaje.observe(viewLifecycleOwner,Observer {
            it?.let {
                binding.txtHeaderOriginCountry.text = it[0].originCountry
                binding.txtHeaderOriginDestiny.text = it[0].destinyCountry
                binding.txtHeaderInitDate.text = it[0].initialDate
                binding.txtHeaderFinishDate.text = it[0].finishDate

            }
        })

        homeTravelViewModel.getRecords(id)
        homeTravelViewModel.recordCounter.observe(viewLifecycleOwner, Observer {
            binding.txtHeaderCatFoodTotal.text = "$"+it["totalFood"].toString()
            binding.txtHeaderCatCarTotal.text = "$"+it["totalTrasport"].toString()
            binding.txtHeaderCatHotelTotal.text = "$"+it["totalHotel"].toString()
            binding.txtHeaderCatOtherTotal.text = "$"+it["others"].toString()

            var balance = homeTravelViewModel.viaje.value?.get(0)?.balance
            if (balance == "" || balance == null) {
                balance = "0.0"
            }
            var balance2 = balance.toDouble()

            balance2 = if (balance2 <= 0.0) {
                (balance2
                        + it["totalFood"]!!.toDouble()
                        + it["totalTrasport"]!!.toDouble()
                        + it["totalHotel"]!!.toDouble()
                        + it["others"]!!.toDouble()
                        )
            } else {
                (balance2
                        - it["totalFood"]!!.toDouble()
                        - it["totalTrasport"]!!.toDouble()
                        - it["totalHotel"]!!.toDouble()
                        - it["others"]!!.toDouble()
                        )
            }
            binding.txtHeaderCash.text = String.format("%.2f", balance2)
            //binding.txtHeaderCash.text = homeTravelViewModel.viaje.value?.get(0)?.balance
        })
    }

    fun email(idTravel: String, context: Context) {
        Timber.d("EMAIL_SHARE %s", idTravel)
        travelRef.document(idTravel).get().addOnSuccessListener {
            val originCountry = it.data?.get("originCountry")?.toString()
            val destinyCountry = it.data?.get("destinyCountry")?.toString()
            val cash = it.data?.get("cash")?.toString()
            val initialDate = it.data?.get("initialDate")?.toString()
            val finishDate = it.data?.get("finishDate")?.toString()
            val description = it.data?.get("description")?.toString()
            //balance = it.data?.get("balance")?.toString()
            travelRef.document(idTravel).collection("record").get()
                .addOnSuccessListener { querySnapShot ->
                    val csv =
                        (Environment.getExternalStorageDirectory().absolutePath + "/Travel_${originCountry}_to_${destinyCountry}_${firebaseUser!!.email}.csv")//Nombre del archivo.csv
                    val write: CSVWriter?
                    write = CSVWriter(FileWriter(csv))
                    val dataHeader =
                        arrayOf<String>("VIAJE", "Realizado por", "${firebaseUser.email}")
                    val dataHeaderTravelcsv = arrayOf(
                        "Pais origen",
                        "Pais destino",
                        "Efectivo asignado",
                        "Fecha inicio",
                        "Fecha fin",
                        "Descripcion"
                    )
                    val dataTravel = arrayOf(
                        originCountry, destinyCountry, cash,
                        initialDate, finishDate, description
                    )
                    val dataHeaderRecord = arrayOf("REGISTROS")
                    val dataHeaderRecords = arrayOf(
                        "Nombre",
                        "Fecha",
                        "Costo",
                        "Categoria",
                        "Enlace de la imagen",
                        "Descripcion"
                    )

                    write.writeNext(dataHeader)
                    write.writeNext(dataHeaderTravelcsv)
                    write.writeNext(dataTravel)
                    write.writeNext(dataHeaderRecord)
                    write.writeNext(dataHeaderRecords)
                    val records = querySnapShot.toObjects(Record::class.java)
                    var totalFoodC = 0.0
                    var totalCarC = 0.0
                    var totalhotelC = 0.0
                    var totalOtherC = 0.0
                    var category :String?=""
                    //obtengo todos los registros de gastos del viaje
                    for (i in 0 until querySnapShot.count()) { //count me da el total de registros
                        when (querySnapShot.documents[i].data!!["recordCategory"].toString()) { //verifco la categoria a la que pertecene cada gasto
                            "0" -> {//si es comida acumula su cantidad en una variable
                                totalFoodC += querySnapShot.documents[i].data!!["recordMount"].toString()
                                    .toDouble()
                                category = "Comida"
                            }
                            "1" -> { // transporte
                                totalCarC += querySnapShot.documents[i].data!!["recordMount"].toString()
                                    .toDouble()
                                category = "Transporte"
                            }
                            "2" -> { //hospedaje
                                totalhotelC += querySnapShot.documents[i].data!!["recordMount"].toString()
                                    .toDouble()
                                category = "Hospedaje"
                            }
                            "3" -> { //Otros
                                totalOtherC += querySnapShot.documents[i].data!!["recordMount"].toString()
                                    .toDouble()
                                category = "Otros"
                            }
                        }

                        dataRecords = arrayOf(
                            records[i].recordName,
                            records[i].recordDate,
                            records[i].recordMount,
                            category,
                            records[i].recordPhoto,
                            records[i].recordDescription
                        )
                        write.writeNext(dataRecords)
                    }

                        balance = ( totalFoodC + totalCarC + totalhotelC + totalOtherC).toString()

                    val dataTotal = arrayOf("TOTAL DE GASTOS", "", "$balance")
                    write.writeNext(dataTotal)
                    balance = (balance!!.toDouble() - cash!!.toDouble()).toString()

                    var finalMount=0.00
                    var subFinalMount=0.00
                    if (balance!!.toDouble()<=0.00) {
                        finalMount=balance!!.toDouble()*(-1)
                    }else{
                        subFinalMount=balance!!.toDouble()
                    }
                    val rtnmoney= arrayOf("TOTAL A DEVOLVER","","$finalMount")
                    val refound= arrayOf("REINTEGRAR","","$subFinalMount")
                    write.writeNext(rtnmoney)
                    write.writeNext(refound)
                    write.close()

                    //envio por correo
                    val emailIntent = Intent(Intent.ACTION_SEND)
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Asunto")
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Escriba aqui")
                    emailIntent.type = "message/rfc822"
                    val file = File(csv)
                    val uri = FileProvider.getUriForFile(
                        context,
                        context.applicationContext.packageName + ".fileprovider",
                        file
                    )
                    emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    emailIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

                    val activities: List<ResolveInfo> =
                        context.packageManager.queryIntentActivities(
                            emailIntent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    val isIntentSafe: Boolean = activities.isNotEmpty()
                    if (isIntentSafe) {
                        startActivity(Intent.createChooser(emailIntent, "Escoger una aplicacion:"))
                    }
                }
        }

    }

    private fun permissionValidation(): Boolean {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            return true
        }
        if ((ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED)&&
            (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)&&
            (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)){
            return true
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                Manifest.permission.CAMERA
            )|| ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!, Manifest.permission.READ_EXTERNAL_STORAGE
            )){
            permissionDialog()

        }else{requestPermissions(arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ),100)}

        return false
    }

    private fun permissionDialog(){
        val dialogPermission: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        dialogPermission.setTitle("Permisos Desactivados")
        dialogPermission.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app")
        dialogPermission.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){ _: DialogInterface, _: Int ->
            requestPermissions(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),100)
        })
        dialogPermission.show()
    }

}