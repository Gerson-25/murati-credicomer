package sv.com.credicomer.murati.ui.alliance.adapters

import androidx.recyclerview.widget.RecyclerView

import sv.com.credicomer.murati.databinding.ListRowCategoriesBinding
import sv.com.credicomer.murati.ui.alliance.models.CategoryFS

class CategoryFSViewHolder(val binding: ListRowCategoriesBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(item: CategoryFS) {

        binding.category = item

        binding.executePendingBindings()

    }

}



