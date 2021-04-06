package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murativ2.databinding.ListRowRoomDetailScheduleBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.*
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel


class RoomDetailAdapter(var roomDetail:RoomDetail,var resultWrapper: RoomResultWrapper, var user: String, val whiteColor:Int, val viewModel:RoomDetailViewModel, val lifecycleOwner: LifecycleOwner):ListAdapter<ListRoomItem,RoomDetailViewHolder>(

   RoomDetailDiffCallback()) {

    private lateinit var binding: ListRowRoomDetailScheduleBinding

    private lateinit var lifeCycle:LifecycleOwner


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomDetailViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        binding = ListRowRoomDetailScheduleBinding.inflate(inflater, parent,false)
        binding.currentUser =  user
        return RoomDetailViewHolder(binding,roomDetail,resultWrapper, whiteColor, viewModel,lifecycleOwner)

    }

    override fun onBindViewHolder(holder: RoomDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class RoomDetailDiffCallback : DiffUtil.ItemCallback<ListRoomItem>() {
           override fun areItemsTheSame(oldItem: ListRoomItem, newItem: ListRoomItem): Boolean {
               return oldItem == newItem
           }

           @SuppressLint("DiffUtilEquals")
           override fun areContentsTheSame(oldItem: ListRoomItem, newItem: ListRoomItem): Boolean {
               return oldItem == newItem
           }


       }

    class ResultListListener(val clickListener: (hour:String)-> Unit){
        fun onClickAdd(hour: String)=clickListener(hour)

    }

   }



