package sv.com.credicomer.murati.ui.roomsv2.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murati.MainViewModel
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentMyReservationsBinding
import sv.com.credicomer.murati.ui.ride.getDateJoda
import sv.com.credicomer.murati.ui.roomsv2.adapters.ReservationsAdapter
import sv.com.credicomer.murati.ui.roomsv2.dialog.DatePickerRoom
import sv.com.credicomer.murati.ui.roomsv2.getParsedDate
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomDetailViewModel
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomViewModel

class MyReservationsFragment : Fragment() {

    lateinit var binding:FragmentMyReservationsBinding
    lateinit var roomViewModel: RoomViewModel
    lateinit var reservationsViewModel: RoomDetailViewModel
    lateinit var mainViewModel: MainViewModel
    private var reservationsAdapter = ReservationsAdapter(mutableListOf(), mutableListOf(), this,"")
    lateinit var rooms:List<String>
    private var date = getDateJoda()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_reservations,container,false)
        roomViewModel = ViewModelProvider(this).get(RoomViewModel::class.java)
        reservationsViewModel = ViewModelProvider(this).get(RoomDetailViewModel::class.java)
        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }
        reservationsViewModel.collectionPath = mainViewModel.roomCollectionPath.value!!.toString()
        reservationsViewModel.subCollectionPath = mainViewModel.roomSubCollectionPath.value!!.toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reservationsViewModel.getDates(date)
        binding.btnDateSelection.text = date
        binding.btnDateSelection.setOnClickListener {
            showDatePickerDialog()
        }
        val contextFrag = this
        binding.recyclerReservations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reservationsAdapter
        }

        reservationsAdapter.delete = fun(key, roomId, date) {
            showDeleteDialog(key, roomId, date)
        }
        roomViewModel.getAllRooms(reservationsViewModel.collectionPath)
        observeViewModel()


    }

    @Suppress("DEPRECATION")
    fun showDatePickerDialog() {
        var selectedDate: String
        val newFragment =
            DatePickerRoom.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
                // +1 because January is zero
                selectedDate = getParsedDate(day, (month + 1), year)
                date=selectedDate
                reservationsViewModel.getDates(date)
                binding.btnDateSelection.text = date
                reservationsAdapter.getDate(date)
            })
        newFragment.show(fragmentManager!!, "datePicker")
    }

    private fun showDeleteDialog(key:String, roomId:String, date:String){
        val alertDialog = context?.let {
            AlertDialog.Builder(it)
                .setTitle("Eliminar Reservacion")
                .setMessage("¿Esta seguro de eliminar esta reservacion?")
                .setPositiveButton("ELIMINAR"){_, _ ->
                    reservationsViewModel.deleteReservation(key, roomId,date, "none")
                    reservationsViewModel.getDates(date)
                    Toast.makeText(context, "Reservacion Eliminada", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("CANCELAR"){_, _ ->
                   }
        }
        alertDialog!!.show()
    }

   fun observeViewModel(){
        reservationsViewModel.reservationList.observe(viewLifecycleOwner, Observer {
            roomViewModel.rooms.observe(viewLifecycleOwner, Observer {
                reservationsAdapter.updateRooms(it)
            })
        })

       reservationsViewModel.reservationList.observe(viewLifecycleOwner, Observer {
           reservationsAdapter.updateReservations(it)
           if(it.size > 0){
               binding.emptyEstate.visibility = View.GONE
               binding.loadingData.visibility = View.GONE
               binding.textNoReservations.visibility = View.GONE
           }
           else{
               binding.loadingData.visibility = View.GONE
               binding.emptyEstate.visibility = View.VISIBLE
               binding.textNoReservations.visibility = View.VISIBLE
           }
       })

    }
}
