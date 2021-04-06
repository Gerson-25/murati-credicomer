package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowHistoryRoomBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.ReservationList
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

class RoomHistoryViewHolder(val binding: ListRowHistoryRoomBinding, var dateRoom:String,var roomsId:String, var roomDetailViewModel: RoomDetailViewModel, var collection:String,var subCollection:String):
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ReservationList){
        binding.reservation=item
        binding.dateRoom=dateRoom
        binding.roomId=roomsId
        binding.roomDetailViewModel=roomDetailViewModel
        binding.collection=collection
        binding.subCollection=subCollection
        binding.executePendingBindings()
    }
}