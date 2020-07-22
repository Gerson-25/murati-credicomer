package sv.com.credicomer.murati.ui.travel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowNewRecordBinding
import sv.com.credicomer.murati.ui.travel.models.Record

class HomeTravelAdapter(var id:String): ListAdapter<Record, HomeTravelAdapter.HomeTravelViewHolder>(HomeTravelDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTravelViewHolder{
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListRowNewRecordBinding.inflate(layoutInflater, parent, false)
        return HomeTravelViewHolder(binding)

    }
    override fun onBindViewHolder(holder: HomeTravelViewHolder, position: Int){
        val item = getItem(position)
        holder.bind(item,id)
    }

    inner class HomeTravelViewHolder(val binding: ListRowNewRecordBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:Record, status: String){
            binding.record = item
            binding.id = id
            binding.executePendingBindings()
        }
    }
}

class HomeTravelDiffCallback: DiffUtil.ItemCallback<Record>(){
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.recordDateRegister == newItem.recordDateRegister
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}