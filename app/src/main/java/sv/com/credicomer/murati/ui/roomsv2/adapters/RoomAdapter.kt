package sv.com.credicomer.murati.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murati.databinding.ListRowRoomBinding
import sv.com.credicomer.murati.ui.roomsv2.models.Room

class RoomAdapter (var date:String): ListAdapter<Room, RoomViewHolder>(
    RoomDiffCallback()){

    private lateinit var binding: ListRowRoomBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        binding = ListRowRoomBinding.inflate(inflater, parent,false)
        return RoomViewHolder(binding,date)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class RoomDiffCallback : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.roomId === newItem.roomId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }

    }

}