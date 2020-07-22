package sv.com.credicomer.murati.ui.alliance.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sv.com.credicomer.murati.databinding.ListRowPromotionBinding
import sv.com.credicomer.murati.ui.alliance.models.PromotionFS



class PromotionsAdapter():ListAdapter<PromotionFS,PromotionFSViewHolder>(PromotionFSDiffCallback()) {
    private lateinit var binding:ListRowPromotionBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionFSViewHolder {
        val inflater=LayoutInflater.from(parent.context)
      binding=ListRowPromotionBinding.inflate(inflater, parent,false)
        return PromotionFSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromotionFSViewHolder, position: Int) {

        val item = getItem(position)
      holder.bind(item)
    }



    class PromotionFSDiffCallback : DiffUtil.ItemCallback<PromotionFS>() {
        override fun areItemsTheSame(oldItem: PromotionFS, newItem: PromotionFS): Boolean {
            return oldItem.promotion_id === newItem.promotion_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PromotionFS, newItem: PromotionFS): Boolean {
            return oldItem == newItem
        }


    }


}