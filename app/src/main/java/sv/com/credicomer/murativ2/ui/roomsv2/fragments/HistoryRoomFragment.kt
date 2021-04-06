package sv.com.credicomer.murativ2.ui.roomsv2.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentHistoryRoomBinding
import sv.com.credicomer.murativ2.ui.roomsv2.adapters.RoomHistoryAdapter
import sv.com.credicomer.murativ2.ui.roomsv2.models.HistoryRoom
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel
import timber.log.Timber

class HistoryRoomFragment : Fragment() {
    private lateinit var binding: FragmentHistoryRoomBinding
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_history_room, container, false)
        mainViewModel =
            activity.run { ViewModelProvider(requireActivity()).get(MainViewModel::class.java) }

        val collection = mainViewModel.roomCollectionPath.value.toString()
        val subCollection = mainViewModel.roomSubCollectionPath.value.toString()

        val args=HistoryRoomFragmentArgs.fromBundle(arguments!!)
        val nameRoom = args.NameRoom
        val detailsRoom= args.Reservations

        if (detailsRoom!!.roomDetail.isNullOrEmpty()){
            binding.emptyStateRoomNoHistoryReservation.visibility = View.VISIBLE
        }

        val listMap : MutableList<HistoryRoom> = mutableListOf()

        detailsRoom?.roomDetail?.forEach {
            listMap.add(HistoryRoom(it.key,it.value))

        }
        val dateRoom = args.reservationDate
        val roomId=args.roomsId

        val historyAdapter = RoomHistoryAdapter(dateRoom,roomId, roomDetailViewModel= RoomDetailViewModel(),
            collection = collection,subCollection=subCollection)

        Timber.d("RECYCLER_RESERVATIONS %s", "$listMap")


       binding.roomName.text= nameRoom.capitalize()
        binding.recyclerHistoryRoom.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerHistoryRoom.adapter = historyAdapter

        return binding.root
    }



}
