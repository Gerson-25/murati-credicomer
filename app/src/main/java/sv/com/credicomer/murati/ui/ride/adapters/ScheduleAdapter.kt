package sv.com.credicomer.murati.ui.ride.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murati.databinding.ItemListBinding


class ScheduleAdapter: ListAdapter<String, ScheduleViewHolder>(PersonDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {

        val layoutInflater= LayoutInflater.from(parent.context)
        val binding= ItemListBinding.inflate(layoutInflater,parent,false)
        return ScheduleViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val item= getItem(position)
        holder.bind(item)

    }


}

class PersonDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}