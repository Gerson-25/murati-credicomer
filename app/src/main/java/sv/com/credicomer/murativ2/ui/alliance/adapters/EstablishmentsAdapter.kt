package sv.com.credicomer.murativ2.ui.alliance.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.LisRowEstablishmentsBinding
import sv.com.credicomer.murativ2.ui.alliance.models.EstablishmentFS

class EstablishmentsAdapter(val category:String): ListAdapter<EstablishmentFS,EstablishmentsAdapter.EstablishmentsViewHolder>(EstablishmentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): EstablishmentsViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LisRowEstablishmentsBinding.inflate(layoutInflater,parent,false)
        return EstablishmentsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: EstablishmentsViewHolder, position: Int){
        val item = getItem(position)
        holder.bind(item)
    }


    inner class EstablishmentsViewHolder(val binding: LisRowEstablishmentsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: EstablishmentFS){
            binding.establishment = item
            binding.category = category
            binding.executePendingBindings()
        }
    }
}

class EstablishmentDiffCallback : DiffUtil.ItemCallback<EstablishmentFS>(){
    override fun areItemsTheSame(oldItem: EstablishmentFS, newItem: EstablishmentFS): Boolean {
        return  oldItem.establishment_id == newItem.establishment_id
    }

    override fun areContentsTheSame(oldItem: EstablishmentFS, newItem: EstablishmentFS): Boolean {
        return  oldItem ==newItem
    }
}