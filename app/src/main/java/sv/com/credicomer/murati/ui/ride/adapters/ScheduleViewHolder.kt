package sv.com.credicomer.murati.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ItemListBinding

class ScheduleViewHolder(val binding: ItemListBinding):RecyclerView.ViewHolder(binding.root) {


    fun bind(item:String){
        binding.schedule=item
        binding.executePendingBindings()

    }
}