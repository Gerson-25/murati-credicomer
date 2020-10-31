package sv.com.credicomer.murativ2.ui.alliance.adapters

import androidx.recyclerview.widget.RecyclerView

import sv.com.credicomer.murativ2.databinding.ListRowCategoriesBinding
import sv.com.credicomer.murativ2.ui.alliance.models.CategoryFS

class CategoryFSViewHolder(val binding: ListRowCategoriesBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(item: CategoryFS) {

        binding.category = item

        binding.executePendingBindings()

    }

}



