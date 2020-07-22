package sv.com.credicomer.murati.ui.roomsv2.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowRoomDetailScheduleBinding
import sv.com.credicomer.murati.ui.roomsv2.models.*
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomDetailViewModel

class RoomDetailViewHolder(val binding:ListRowRoomDetailScheduleBinding,val roomDetail: RoomDetail,var resultWrapper:RoomResultWrapper, val color:Int, val viewModel:RoomDetailViewModel):RecyclerView.ViewHolder(binding.root) {


    fun bind(item: ListRoomItem){
        binding.viewModel = viewModel
        binding.colorText = color
        binding.schedule=item
        binding.roomDetail=roomDetail
        binding.resultWrapper=resultWrapper
        binding.adapterPosition=adapterPosition
        binding.executePendingBindings()

    }

}