package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowRoomBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.Room

class RoomViewHolder(var binding:ListRowRoomBinding, val date:String):RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Room){
        binding.room=item
        binding.date=date
        binding.executePendingBindings()

    }
}