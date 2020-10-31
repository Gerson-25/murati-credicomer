package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murativ2.databinding.ListRowHistoryRoomBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.ReservationList
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

class RoomHistoryAdapter(var dateRoom:String, var roomsId:String, var roomDetailViewModel: RoomDetailViewModel, var collection:String,var subCollection:String):ListAdapter<ReservationList,RoomHistoryViewHolder>(RoomHistoryDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHistoryViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRowHistoryRoomBinding.inflate(inflater, parent,false)
        return RoomHistoryViewHolder(binding,dateRoom,roomsId,roomDetailViewModel,collection,subCollection)
    }

    override  fun onBindViewHolder(holder: RoomHistoryViewHolder,position: Int){
        val item = getItem(position)
        holder.bind(item)
    }

    class RoomHistoryDiffCallback : DiffUtil.ItemCallback<ReservationList>(){
        override fun areItemsTheSame(oldItem: ReservationList, newItem: ReservationList): Boolean {
            return oldItem.date == newItem.date
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ReservationList,
            newItem: ReservationList
        ): Boolean {
            return oldItem == newItem
        }
    }

}