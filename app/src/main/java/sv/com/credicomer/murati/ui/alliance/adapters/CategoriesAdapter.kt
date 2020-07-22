package sv.com.credicomer.murati.ui.alliance.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import sv.com.credicomer.murati.databinding.ListRowCategoriesBinding
import sv.com.credicomer.murati.ui.alliance.models.CategoryFS




class CategoriesAdapter() :
    ListAdapter<CategoryFS, CategoryFSViewHolder>(CategoryFSDiffCallback()) {

    private lateinit var binding: ListRowCategoriesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFSViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ListRowCategoriesBinding.inflate(inflater, parent, false)
        return CategoryFSViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CategoryFSViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class CategoryFSDiffCallback : DiffUtil.ItemCallback<CategoryFS>() {
        override fun areItemsTheSame(oldItem: CategoryFS, newItem: CategoryFS): Boolean {
            return oldItem.category_id === newItem.category_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: CategoryFS, newItem: CategoryFS): Boolean {
            return oldItem == newItem
        }


    }


}