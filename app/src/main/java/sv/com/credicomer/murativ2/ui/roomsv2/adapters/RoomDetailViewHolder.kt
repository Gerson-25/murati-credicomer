package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowRoomDetailScheduleBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.*
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

class RoomDetailViewHolder(val binding:ListRowRoomDetailScheduleBinding,val roomDetail: RoomDetail,var resultWrapper:RoomResultWrapper, val color:Int, val viewModel:RoomDetailViewModel, val lifecycleOwner: LifecycleOwner):RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    val lifecycleRegistry = LifecycleRegistry(this)

    init {
        lifecycleRegistry.markState(Lifecycle.State.INITIALIZED)
    }
    fun markAttach() {
        // Lifecycle.State.CREATED doesn't work for this case
        // lifecycleRegistry.markState(Lifecycle.State.CREATED)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        // lifecycleRegistry.markState(Lifecycle.State.RESUMED)
    }

    fun markDetach() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun bind(item: ListRoomItem){
        binding.lifecycle = lifecycleOwner
        binding.viewModel = viewModel
        binding.colorText = color
        binding.schedule=item
        binding.roomDetail=roomDetail
        binding.resultWrapper=resultWrapper
        binding.adapterPosition=adapterPosition
        binding.executePendingBindings()

    }



}