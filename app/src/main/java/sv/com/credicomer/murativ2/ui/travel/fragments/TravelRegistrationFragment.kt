package sv.com.credicomer.murativ2.ui.travel.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import butterknife.ButterKnife
import butterknife.Unbinder
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_schedule_list_dialog.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentTravelRegistrationBinding
import sv.com.credicomer.murativ2.ui.travel.models.Travel
import sv.com.credicomer.murativ2.ui.travel.viewModel.TravelRegistrationViewModel
import sv.com.credicomer.murativ2.utils.edit_text.EditTextUtils
import java.text.SimpleDateFormat
import java.util.*

class TravelRegistrationFragment : Fragment() {

    private lateinit var travelRegistrationViewModel: TravelRegistrationViewModel
    private lateinit var navController: NavController

    private lateinit var binding: FragmentTravelRegistrationBinding
    private val user = FirebaseAuth.getInstance().currentUser
    private var emailUser: String?=null
    //variables para Date picker
    var mDateStart: String? = null
    var mDateEnd: String? = null
    private var unbinder: Unbinder? = null
    //variables de los parametros obtenidos del argumento
    private var id: String? = null
    private var persist :String?=null
    var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_travel_registration,container,false)
        travelRegistrationViewModel = ViewModelProvider(this).get(TravelRegistrationViewModel::class.java)
        navController = findNavController()

        //getingArguments
        val args = arguments?.let { TravelRegistrationFragmentArgs.fromBundle(it) }

        id=args?.id
        persist = args?.date

        //AutocompleteTextview
        val countries = resources.getStringArray(R.array.coutries_array)
        val departure_list = resources.getStringArray(R.array.departures_array)
        val destinations_list = resources.getStringArray(R.array.destinations_array)
        val contriAdapter= ArrayAdapter(activity!!,android.R.layout.simple_list_item_1,countries)
        val departureAdapter = ArrayAdapter(activity!!, android.R.layout.simple_expandable_list_item_1, departure_list)
        val destinationAdapter = ArrayAdapter(activity!!, android.R.layout.simple_expandable_list_item_1, destinations_list)
        binding.editTextOrigin.setAdapter(departureAdapter)
        binding.editTextDestiny.setAdapter(destinationAdapter)
        //Finish autocomplate

        //Date picker
        view?.let { ButterKnife.bind(this, it) }
        unbinder = view?.let { ButterKnife.bind(this, it) }

        binding.editTextOrigin.filters = EditTextUtils.getOnlyLetter()
        binding.editTextDestiny.filters = EditTextUtils.getOnlyLetter()

        binding.textViewPickInitialDate.setOnClickListener{
            openDateRangePicker()
        }

        val pager = mutableListOf<ViewGroup>(
            //binding.locationContainer
            //binding.dateContainer,
            //binding.messageContainer
        )

        var position = 0

        binding.textViewPickFinalDate.setOnClickListener{
            openDateRangePicker()
        }
        //finish Date picker

        binding.editTextCost.filters = EditTextUtils.getDecimalNumber()

        //COLOCAR CODIGO PARA ACTUALIZAR DATA.

        //condicional para entrar a crear un nuevo viaje o para actualizar el viaje
        if (id == "") {
            binding.buttonRegistrations.setOnClickListener {
                registration()
            }
        }
        else{
            binding.buttonRegistrations.text= getString(R.string.update_travel) //Cambio de texto del boton
            getTravels(id!!) //Obtencion de la data
            binding.buttonRegistrations.setOnClickListener{
                updateTravel(id!!,persist!!)
            } //Actualizacion de la data
        }

        binding.buttonRegistrations.setOnClickListener {
            registration()
        }

        return binding.root

    }

    private fun slider(reducer: Button, increaser: Button, pager: MutableList<ViewGroup>){
        reducer.setOnClickListener {
            pager.forEach {
                it.visibility = View.GONE
            }
            if (position  > 0){
                position -= 1
                if (position == 0){
                    reducer.visibility = View.GONE
                }else{
                    reducer.visibility = View.VISIBLE
                }
                pager[position].visibility = View.VISIBLE
            }
        }
        increaser.setOnClickListener {
            if (position  < 2){
                pager.forEach {
                    it.visibility = View.GONE
                }
                position += 1
                reducer.visibility = View.VISIBLE
                if (position == 2){
                    increaser.text = "EMPEZAR"
                }
                pager[position].visibility = View.VISIBLE
            }
        }
    }

    private fun openDateRangePicker(){
        Toast.makeText(context, "Presiona y delisza para elegir el rango de fechas", Toast.LENGTH_SHORT).show()//Metodo para abrir el calendario
        val pickerFrag = DatePickerFragment()
        pickerFrag.setCallback(object : DatePickerFragment.Callback{
            override fun onCancelled(){
                Toast.makeText(activity,getString(R.string.User_cancel_datapicker), Toast.LENGTH_SHORT).show()
            }
            override fun onDateTimeRecurrenceSet(selectedDate: SelectedDate, hourOfDay: Int, minute: Int,
                                                 recurrenceOption: SublimeRecurrencePicker.RecurrenceOption,
                                                 recurrenceRule: String?) {
                @SuppressLint("SimpleDateFormat")
                val formatDate = SimpleDateFormat("dd-MM-yyyy")
                mDateStart = formatDate.format(selectedDate.startDate.time)
                mDateEnd = formatDate.format(selectedDate.endDate.time)

                val initdate = mDateStart
                val finishdate = mDateEnd

                binding.textViewPickInitialDate.text=initdate
                binding.textViewPickFinalDate.text=finishdate
            }
        })

        // inicio de configuracion de library sublime para method Date Range Picker
        val options = SublimeOptions()
        options.setCanPickDateRange(true)
        options.pickerToShow = SublimeOptions.Picker.DATE_PICKER
        val bundle = Bundle()
        bundle.putParcelable("SUBLIME_OPTIONS", options)
        pickerFrag.arguments = bundle
        pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0)
        pickerFrag.show(activity!!.supportFragmentManager, "SUBLIME_PICKER")
    }

    private fun registration() {//Metodo para registrar viaje nuevo
        if (binding.editTextOrigin.text.toString().isEmpty() || binding.editTextDestiny.text.toString().isEmpty()
              || binding.textViewPickInitialDate.text.toString().isEmpty()
        ) {
            Toast.makeText(activity, activity!!.getString(R.string.error_hint), Toast.LENGTH_SHORT)
                .show()
        }
        else {
            val origCountry = binding.editTextOrigin.text.toString()
            val destCountry = binding.editTextDestiny.text.toString()
            var cassh = binding.editTextCost.text.toString()
            if(cassh ==""){
                cassh ="0.00"
            }
            val datePick = binding.textViewPickInitialDate.text.toString()
            val finishtravel = binding.textViewPickFinalDate.text.toString()
            emailUser=user!!.email
            val email = emailUser
            val date:String = getDateTime().toString()
            val update = null
            val active = true
            val settled = false

            val travel = Travel(
                origCountry,
                destCountry,
                cassh,
                email,
                datePick,
                finishtravel,
                date,
                update,
                cassh,
                active,
                settled)

            travelRegistrationViewModel.sendData(travel,requireContext(),resources)
            travelRegistrationViewModel.isDataSent.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    navController.navigate(TravelRegistrationFragmentDirections.actionTravelRegistrationFragmentToNavHome(it,date,true))
            })


        }
    }

    //obtencion de la data del viaje ingresado
    private fun getTravels(id: String) {
        travelRegistrationViewModel.getData(id)

        travelRegistrationViewModel.viaje.observe( viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {

                binding.editTextOrigin.setText(it[0].originCountry)
                binding.editTextDestiny.setText(it[0].destinyCountry)
                binding.editTextCost.setText(it[0].cash)
                binding.textViewPickInitialDate.text = it[0].initialDate
                binding.textViewPickFinalDate.text = it[0].finishDate
            }
        })
    }
    //Finaliza de llenar los datos en el formulario


    private fun updateTravel(id: String, persist: String){//Creacion del Actualizar Datos del viaje
        if (binding.editTextOrigin.text.toString().isEmpty() || binding.editTextDestiny.text.toString().isEmpty()
            || binding.editTextCost.text.toString().isEmpty() || binding.textViewPickInitialDate.text.toString().isEmpty()
        ) {
            Toast.makeText(activity, activity!!.getString(R.string.error_hint), Toast.LENGTH_SHORT)
                .show()
        }
        else {
            val origCountry = binding.editTextOrigin.text.toString()
            val destCountry = binding.editTextDestiny.text.toString()
            val cassh = binding.editTextCost.text.toString()
            val datePick = binding.textViewPickInitialDate.text.toString()
            val finishtravel = binding.textViewPickFinalDate.text.toString()
            emailUser=user!!.email
            val email = emailUser
            val update = getDateTime()
            val active = true
            val settled = false

            val travel = Travel(
                origCountry,
                destCountry,
                cassh, email,
                datePick,finishtravel, persist,update,
                cassh,active, settled,id
            )
            travelRegistrationViewModel.updateData(travel,id,requireContext(),resources)

                navController.navigate(TravelRegistrationFragmentDirections.actionTravelRegistrationFragmentToNavHome(id,persist,true))


        }
    }

    //obtencion de la fecha actual
    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(): String? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
            sdf.format(Calendar.getInstance().time)
        } catch (e: Exception) {
            e.toString()
        }
    }

}
