package sv.com.credicomer.murativ2.ui.alliance.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowPromotionBinding
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS

class PromotionFSViewHolder(val binding:ListRowPromotionBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind(item:PromotionFS){

        binding.promotion=item
        binding.executePendingBindings()

    }



}