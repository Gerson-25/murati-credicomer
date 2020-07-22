package sv.com.credicomer.murati.ui.travel.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowOldTravelBinding
import sv.com.credicomer.murati.ui.travel.models.Travel
import sv.com.credicomer.murati.ui.travel.viewModel.HomeViewModel

class HistoryTravelAdapter(var viewModel: HomeViewModel): ListAdapter<Travel, HistoryTravelAdapter.HistoryTravelViewHolder>(HistoryTravelDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTravelViewHolder {

        val layoutInflater= LayoutInflater.from(parent.context)
        val binding=ListRowOldTravelBinding.inflate(layoutInflater,parent,false)
        return HistoryTravelViewHolder(binding)

    }

    override fun onBindViewHolder(holder: HistoryTravelViewHolder, position: Int) {
        val item= getItem(position)
        holder.bind(item,viewModel)

    }
    inner class HistoryTravelViewHolder(val  binding: ListRowOldTravelBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Travel,viewModel: HomeViewModel){
            binding.travel = item
            binding.viewModel=viewModel
            binding.executePendingBindings()
        }

    }


}

class HistoryTravelDiffCallback : DiffUtil.ItemCallback<Travel>() {
    override fun areItemsTheSame(oldItem: Travel, newItem: Travel ): Boolean {
        return oldItem.emailUser == newItem.emailUser
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Travel, newItem: Travel): Boolean {
        return oldItem == newItem
    }



}
