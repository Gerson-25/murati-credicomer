package sv.com.credicomer.murati.ui.roomsv2.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowRoomBinding
import sv.com.credicomer.murati.ui.roomsv2.models.Room

class RoomViewHolder(var binding:ListRowRoomBinding, val date:String):RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Room){
        binding.room=item
        binding.date=date
        binding.executePendingBindings()

    }
}