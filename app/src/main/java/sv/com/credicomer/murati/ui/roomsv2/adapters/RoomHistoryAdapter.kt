package sv.com.credicomer.murati.ui.roomsv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murati.databinding.ListRowHistoryRoomBinding
import sv.com.credicomer.murati.ui.roomsv2.models.HistoryRoom
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomDetailViewModel

class RoomHistoryAdapter(var dateRoom:String, var roomsId:String, var roomDetailViewModel: RoomDetailViewModel, var collection:String,var subCollection:String):ListAdapter<HistoryRoom,RoomHistoryViewHolder>(RoomHistoryDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHistoryViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListRowHistoryRoomBinding.inflate(inflater, parent,false)
        return RoomHistoryViewHolder(binding,dateRoom,roomsId,roomDetailViewModel,collection,subCollection)
    }

    override  fun onBindViewHolder(holder: RoomHistoryViewHolder,position: Int){
        val item = getItem(position)
        holder.bind(item)
    }

    class RoomHistoryDiffCallback : DiffUtil.ItemCallback<HistoryRoom>(){
        override fun areItemsTheSame(oldItem: HistoryRoom, newItem: HistoryRoom): Boolean {
            return oldItem.roomReservation.email == newItem.roomReservation.email
        }

        override fun areContentsTheSame(
            oldItem: HistoryRoom,
            newItem:HistoryRoom
        ): Boolean {
            return oldItem == newItem
        }
    }

}