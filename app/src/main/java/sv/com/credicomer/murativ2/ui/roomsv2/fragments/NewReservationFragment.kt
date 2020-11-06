package sv.com.credicomer.murativ2.ui.roomsv2.fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.google.android.material.chip.Chip
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentNewReservationBinding
import sv.com.credicomer.murativ2.ui.roomsv2.dialog.UsersDialog
import sv.com.credicomer.murativ2.ui.roomsv2.models.Room
import sv.com.credicomer.murativ2.ui.roomsv2.models.ScheduleList
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel
import java.util.*
import kotlin.collections.HashMap


class NewReservationFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var binding: FragmentNewReservationBinding
    lateinit var viewModel: RoomDetailViewModel
    lateinit var chipList: List<Chip>
    lateinit var room: Room
    lateinit var date:Calendar
    lateinit var time:Calendar
    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = arguments?.let {RoomDetailFragmentArgs.fromBundle(it)
        }

        room = args!!.room


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_reservation, container, false)

        viewModel = activity.run {
            ViewModelProvider(requireActivity()).get(RoomDetailViewModel::class.java)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = Calendar.getInstance()
        time = Calendar.getInstance()

        viewModel.getEmails()

        binding.loadingContainer.setOnClickListener {
            Toast.makeText(context, "Reservando...", Toast.LENGTH_SHORT).show()
        }

        viewModel.emails.observe(viewLifecycleOwner, Observer {
            initSpinners(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.loadingContainer.visibility = View.VISIBLE
            }
            else{
                binding.loadingContainer.visibility = View.GONE
                binding.inputEndTime.text = ""
                binding.inputStartTime.text = ""
                binding.meetingTitle.text.clear()
                binding.inputSetDate.text = ""
                chipList.forEach {
                    it.visibility = View.GONE
                }
            }
        })

        clearInputs()

        chipList = listOf<Chip>(
            binding.chipOne,
            binding.chipTwo,
            binding.chipThree,
            binding.chipFour,
            binding.chipFive,
            binding.chipSix,
            binding.chipSeven,
            binding.chipEigth
        )

        var emailsCounter = 0

        binding.buttonCloseCalendar.setOnClickListener {
            binding.calendar.visibility = View.GONE
        }

        date.add(Calendar.DATE, 0)
        val dateDialog = DatePickerDialog(activity!!,this, date.year,date.month,date.dayOfMonth )
        dateDialog.datePicker.minDate = date.timeInMillis
        date.add(Calendar.DATE, 14)
        dateDialog.datePicker.maxDate = date.timeInMillis

        binding.inputSetDate.setOnClickListener {
            dateDialog.show()
        }

        binding.calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
            binding.inputSetDate.text = "$i3-${i2+1}-$i"
        }

        binding.timepickerStart.setOnTimeChangedListener { timePicker, i, i2 ->
            binding.inputStartTime.text = "$i:$i2"
        }

        binding.timepickerEnd.setOnTimeChangedListener { timePicker, i, i2 ->
            binding.inputEndTime.text = "$i:$i2"
        }

        binding.roomName.text = room.roomName
        binding.textRoomCapacity.text = """${room.capacity} Personas Maximo"""
        binding.textRoomLevel.text = room.location

        var listOfSchedule = mutableListOf<ScheduleList>()

        viewModel.listOfSchedule.observe(viewLifecycleOwner, Observer { list ->
            listOfSchedule = list
        })

        var listOfScheduleEnd = mutableListOf<ScheduleList>()

        viewModel.listOfScheduleEnd.observe(viewLifecycleOwner, Observer { list ->
            listOfScheduleEnd = list
        })

        binding.inputStartTime.setOnClickListener {
            if (listOfSchedule.isNullOrEmpty()){
                Toast.makeText(context, "Selecciona la fecha", Toast.LENGTH_SHORT).show()
            }
            else{
                val scheduleDialog = ScheduleListDialog.newInstance(listOfSchedule.toTypedArray(), 1)
                scheduleDialog.show(parentFragmentManager, "schedule dialog")
            }
        }

        binding.inputEndTime.setOnClickListener {
            if (binding.inputStartTime.text.isNullOrEmpty()){
                Toast.makeText(context, "Selecciona hora de inicio", Toast.LENGTH_SHORT).show()
            }else{
                val scheduleDialog = ScheduleListDialog.newInstance(listOfScheduleEnd.toTypedArray(), 2)
                scheduleDialog.show(parentFragmentManager, "schedule dialog")
            }
        }

        binding.chipGroupContainer.setOnClickListener {
            var userDialog = UsersDialog()
            userDialog.show(parentFragmentManager, "user dialog")
        }

        binding.addUserButton.setOnClickListener {
            chipList[emailsCounter].apply {
                visibility = View.VISIBLE
                text = binding.inputEmails.text
                isCheckedIconVisible = false
                emailsCounter++
                binding.inputEmails.text.clear()
                setOnCloseIconClickListener {
                    visibility = View.GONE
                    emailsCounter--
                }
            }
        }

        viewModel.startTime.observe(viewLifecycleOwner, Observer {
            binding.inputEndTime.text = it.split("-")[1]
            binding.inputStartTime.text = it.split("-")[0]
        })

        viewModel.endTime.observe(viewLifecycleOwner, Observer {
            binding.inputEndTime.text = it.split("-")[0]
        })

        binding.inputStartTime.doOnTextChanged { text, start, count, after ->
            viewModel.getEndAvailableSchedule(room.roomId!!, binding.inputSetDate.text.toString(), binding.inputStartTime.text.toString())
        }

        viewModel.listOfEmails.observe(viewLifecycleOwner, Observer {
            var counter = 0
            it.forEach {
                chipList[counter].apply {
                    visibility = View.VISIBLE
                    text = it
                    isCheckedIconVisible = false
                    binding.inputEmails.text.clear()
                    setOnCloseIconClickListener {
                        visibility = View.GONE
                    }
                }
                counter++
            }
        })

        binding.buttonReserve.setOnClickListener {
            if (checkInputs()){
                Toast.makeText(context, "Reservacion completa", Toast.LENGTH_SHORT).show()
                val newReservation = setReservationValues(takeEmails())
                viewModel.saveNewReservation(room, newReservation)
            }
            else{
                Toast.makeText(context, "Hay campos incompletos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.timepickerStart.setIs24HourView(true)
        binding.timepickerEnd.setIs24HourView(true)
    }

    fun toast(message:String) =
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

    fun initSpinners(emails:List<String>) {
        ArrayAdapter(activity!!, android.R.layout.simple_spinner_item, emails).also {
            //it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.inputEmails.apply {
                setAdapter(it)
                threshold = 3
            }
        }
    }

    fun checkInputs():Boolean{
        return binding.inputStartTime.text.isNotEmpty() && binding.inputEndTime.text.isNotEmpty()
                && binding.inputSetDate.text.isNotEmpty() && binding.meetingTitle.text.isNotEmpty()
    }

    fun takeEmails():MutableList<String>{

        var emails = mutableListOf<String>()
        chipList.forEach {
            if (it.visibility == View.VISIBLE){
                emails.add(it.text.toString())
            }
        }
        return emails
    }

    fun clearInputs(){
        binding.inputStartTime.text = ""
        binding.inputEndTime.text = ""
    }

    fun setReservationValues(emails:MutableList<String>):HashMap<String, Any>{
        val newReservation = hashMapOf(
            "room" to binding.roomName.text.toString(),
            "title" to binding.meetingTitle.text.toString(),
            "date" to binding.inputSetDate.text.toString(),
            "start" to binding.inputStartTime.text.toString(),
            "end" to binding.inputEndTime.text.toString(),
            "attendees" to emails
        )
        return newReservation
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        var day: String = if (p3 < 10){
            "0${p3}"
        } else{
            p3.toString()
        }
        var month: String = if (p2+1 < 10){
            "0${p2+1}"
        } else{
            (p2+1).toString()
        }

        val date = "$day-$month-$p1"
        viewModel.getAvailableSchedules(room.roomId!!, date )
        binding.inputSetDate.text = date
        binding.inputEndTime.text = ""
        binding.inputStartTime.text = ""
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
    }


}