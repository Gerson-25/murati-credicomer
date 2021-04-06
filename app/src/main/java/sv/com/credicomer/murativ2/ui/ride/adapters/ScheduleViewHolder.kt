package sv.com.credicomer.murativ2.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ItemListBinding

class ScheduleViewHolder(val binding: ItemListBinding):RecyclerView.ViewHolder(binding.root) {


    fun bind(item:String){
        binding.schedule=item
        binding.executePendingBindings()

    }
}