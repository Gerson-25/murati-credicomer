package sv.com.credicomer.murativ2.ui.roomsv2.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.date.month
import com.afollestad.date.year
import com.bumptech.glide.Glide
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentRoomDetailBinding
import sv.com.credicomer.murativ2.ui.ride.getDateJoda
import sv.com.credicomer.murativ2.ui.roomsv2.*
import sv.com.credicomer.murativ2.ui.roomsv2.adapters.DateAdapter
import sv.com.credicomer.murativ2.ui.roomsv2.adapters.RoomHistoryAdapter
import sv.com.credicomer.murativ2.ui.roomsv2.models.*
import sv.com.credicomer.murativ2.ui.roomsv2.models.Date
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel
import timber.log.Timber
import java.util.*

class RoomDetailFragment : Fragment() {

    private lateinit var binding: FragmentRoomDetailBinding
    private lateinit var roomDetailViewModel: RoomDetailViewModel
    private lateinit var dateAdapter: DateAdapter
    private var resultList: MutableList<RoomResult> = mutableListOf()
    private var resultWrapper: RoomResultWrapper = RoomResultWrapper(resultList)
    private var date = getDateJoda()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var roomId: String
    private lateinit var navController: NavController
    private var handler = Handler()


    @SuppressLint("DefaultLocale", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
        navController = findNavController()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_detail, container, false)
        roomDetailViewModel = ViewModelProvider(this).get(RoomDetailViewModel::class.java)
        binding.btnRoomDetailDateSelection.text = NOW

        val collection = mainViewModel.roomCollectionPath.value.toString()
        val subCollection = mainViewModel.roomSubCollectionPath.value.toString()

        roomDetailViewModel.collectionPath = mainViewModel.roomCollectionPath.value.toString()
        roomDetailViewModel.subCollectionPath =
            mainViewModel.roomSubCollectionPath.value.toString()

        //phone

        val phoneItem = binding.detailRoomTelefono
        val phoneTitle = binding.textView38
        val phoneContainer = binding.phoneContainer

        //tv

        val tvItem = binding.detailRoomTelevisor
        val tvTitle = binding.textView39
        val tvContainer = binding.tvContainer

        // projector

        val projectorItem = binding.detailRoomProyector
        val projectorTitle = binding.textView40
        val projectorContainer = binding.proyectorContainer

        // whiteboard

        val whiteboardItem = binding.detailRoomPizarra
        val whiteboardTitle = binding.textView41
        val whiteboard = binding.whiteboardContainer

        // webcam

        val webcamItem = binding.detailRoomWebcam
        val webcamTitle = binding.textView42
        val webcamContainer = binding.webcamContainer

        //screen

        val screenItem = binding.detailRoomPantalla
        val screenTitle = binding.textView43
        val screenContainer = binding.screenContainer

        var equipmentItems = arrayListOf<List<View>>(listOf())

        val args = arguments?.let { RoomDetailFragmentArgs.fromBundle(it) }
        val room = args?.room
        //val dat =args?.date
        roomDetailViewModel.increaseReservationCounter("zero")
        binding.room = args?.room
        roomId = room?.roomId.toString()
        room!!.equipment!!.forEach {
            when(it){
                "proyector" -> {
                    projectorItem.setImageResource(R.drawable.ic_room_proyector)
                    projectorItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    projectorTitle.setTextColor(R.color.mColorSecundaryLight)}
                "pizarra" -> {
                    whiteboardItem.setImageResource(R.drawable.ic_room_pizarra)
                    whiteboardItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    whiteboardTitle.setTextColor(R.color.mColorSecundaryLight)}
                "telefono" -> {
                    phoneItem.setImageResource(R.drawable.ic_room_phone_call)
                    phoneItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    phoneTitle.setTextColor(R.color.mColorSecundaryLight)}
                "pantalla" -> {
                    screenItem.setImageResource(R.drawable.ic_room_pantalla_proyector)
                    screenItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    screenTitle.setTextColor(R.color.mColorSecundaryLight)}
                "webcam" -> {
                    webcamItem.setImageResource(R.drawable.ic_room_webcam)
                    webcamItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    webcamTitle.setTextColor(R.color.mColorSecundaryLight)}
                "televisor" -> {
                    tvItem.setImageResource(R.drawable.ic_room_televisor)
                    tvItem.background = resources.getDrawable(R.drawable.bg_room_items)
                    tvTitle.setTextColor(R.color.mColorSecundaryLight)}
                else-> null
            }

        }

        val dateList = arrayListOf<Date>()
        for (number in 0..14){
            val date = Calendar.getInstance()
            date.add(Calendar.DATE, number).toString()
            val dateSplitter = date.time.toString().split(" ")
            val newDate = Date(dateSplitter[2], dateSplitter[0], number == 0, date.time.toString(), (date.month + 1).toString(), date.year.toString())
            dateList.add(newDate)
        }

        binding.txtViewDate.text = date



        dateAdapter = DateAdapter(dateList, roomDetailViewModel, roomId)


        binding.floatingActionButton2.setOnClickListener {
            navController.navigate(RoomDetailFragmentDirections.actionRoomDetailFragmentToNewReservationFragment(room))
        }

        binding.recyclerViewDate.adapter = dateAdapter
        dateAdapter.submitList(dateList)
        binding.recyclerViewDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        roomDetailViewModel.selectedDay.observe(viewLifecycleOwner, Observer {
            val dateListLive = arrayListOf<Date>()
            for (number in 0..14){
                val date = Calendar.getInstance()
                date.add(Calendar.DATE, number).toString()
                val dateSplitter = date.time.toString().split(" ")
                val newDate = Date(dateSplitter[2], dateSplitter[0], number == it-1, date.time.toString(), date.month.toString(), date.year.toString())
                dateListLive.add(newDate)
            }
            dateAdapter.notifyDataSetChanged()
            dateAdapter.submitList(dateListLive)
        })

        binding.btnRoomDetailDateSelection.setOnClickListener {
           showDatePickerDialog()
        }


        roomDetailViewModel.day.observe(viewLifecycleOwner, Observer {
            binding.txtViewDate.text = it
        })

        Glide.with(this).load(room.roomImages!![0]).centerCrop().into(binding.roomDetailImage)
        binding.txtViewRoomDetailRoomName.text = room.roomName.toString().capitalize()


        binding.recyclerViewRoomDetail.layoutManager = LinearLayoutManager(requireContext())

        roomDetailViewModel.schedule.observe(viewLifecycleOwner, Observer {


        })

        roomDetailViewModel.reservationListRT.observe(viewLifecycleOwner, Observer { detail ->
            if (detail.isNullOrEmpty()) {
                binding.noReservationsState.visibility = View.VISIBLE
                binding.recyclerViewRoomDetail.visibility = View.GONE
            }
            else{
                detail.sortBy {
                    it.start
                }
                binding.recyclerViewRoomDetail.visibility = View.VISIBLE
                binding.noReservationsState.visibility = View.GONE
                val historyAdapter = RoomHistoryAdapter(date,roomId, roomDetailViewModel= RoomDetailViewModel(),
                    collection = collection,subCollection=subCollection)
                binding.recyclerViewRoomDetail.adapter = historyAdapter
                historyAdapter.submitList(detail)

            }

            roomDetailViewModel.getSchedule()

        })

        return binding.root
    }

    private fun btnPushReservation(date:String){


        binding.btnRoomDetailPushReservation.setOnClickListener {

            val sortedRoomResultList = sortRoomsFun(resultWrapper.resultList)
            Timber.d("RESULTSORTEDLIST %s", "THE RESULT LIST IS ->${sortedRoomResultList}")
            val reservationList = mergeLists(date, sortedRoomResultList)
            Timber.d("FINALLIST %s", "$reservationList")
            if (reservationList.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Selecciona alguna hora", Toast.LENGTH_LONG)
                    .show()
            } else {
                roomDetailViewModel.pushRoomReservation(reservationList, roomId, date)
                Toast.makeText(requireContext(), "reservacion completada", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun showDatePickerDialog(){

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dateP = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                val selectedDate = getParsedDate(dayOfMonth, (monthOfYear + 1), year)

                // Display Selected date in TextBox
                when (selectedDate) {
                    getDatePlusOneDay() -> {
                        binding.btnRoomDetailDateSelection.text = TOMORROW
                    }
                    getDateJoda() -> {
                        binding.btnRoomDetailDateSelection.text = NOW
                    }
                    else -> {
                        binding.btnRoomDetailDateSelection.text = selectedDate
                    }
                }

                date = selectedDate
                btnPushReservation(date)
                roomDetailViewModel.getRoomReservation(roomId, date)
            },
            year,
            month,
            day
        )

        binding.btnRoomDetailDateSelection.setOnClickListener {
            dateP.datePicker.minDate = c.timeInMillis
            dateP.show()
        }

    }

}