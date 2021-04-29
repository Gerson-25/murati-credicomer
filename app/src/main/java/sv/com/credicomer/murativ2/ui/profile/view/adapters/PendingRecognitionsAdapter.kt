package sv.com.credicomer.murativ2.ui.profile.view.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_birthday_list.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAchievementListBinding
import sv.com.credicomer.murativ2.databinding.ItemBirthdayListBinding
import sv.com.credicomer.murativ2.databinding.ItemPendingMessageBinding
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel

class PendingRecognitionsAdapter(var list:MutableList<Recognition>, var users: List<UserCarnet>):RecyclerView.Adapter<PendingRecognitionsAdapter.BirthDayViewHolder>() {

    private lateinit var recognitionHandler:RecognitionHandler

    fun setNewData(newValues:MutableList<Recognition>){
        list.clear()
        list.addAll(newValues)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemPendingMessageBinding>(inflater, R.layout.item_pending_message, parent, false)
        return BirthDayViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BirthDayViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        //holder.view.user =
        recognitionHandler = RecognitionHandler()
        val senderData = recognitionHandler.getSenderData(list[position].sender!!, users)
        val receiverData = recognitionHandler.getReceiverData(list[position].receiver!!, users)

        holder.itemView.sender.text = senderData.name
        holder.itemView.receiver.text = "enviado a ${receiverData[0].name} y ${list[position].receiver!!.size - 1} personas mas"
        holder.itemView.message.text = list[position].message
        if (list[position].like!!.contains("")){
            holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_favorite_border_green)
        }
        holder.itemView.user_photo.apply {
            Glide.with(this).load(senderData.carnetPhoto).into(this)
        }
        holder.itemView.date_message.text = list[position].date

    }


    inner class  BirthDayViewHolder(var view: ItemPendingMessageBinding):RecyclerView.ViewHolder(view.root){
    }
}