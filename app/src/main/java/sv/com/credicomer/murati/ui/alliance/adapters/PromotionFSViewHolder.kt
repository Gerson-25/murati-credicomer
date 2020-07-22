package sv.com.credicomer.murati.ui.alliance.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowPromotionBinding
import sv.com.credicomer.murati.ui.alliance.models.PromotionFS

class PromotionFSViewHolder(val binding:ListRowPromotionBinding):RecyclerView.ViewHolder(binding.root) {

    fun bind(item:PromotionFS){

        binding.promotion=item
        binding.executePendingBindings()

    }



}