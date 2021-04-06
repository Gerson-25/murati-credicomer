package sv.com.credicomer.murativ2.ui.profile.model

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_birthday_list.view.*
import sv.com.credicomer.murativ2.MainViewModel
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel

class BirthdayAdapter(var list:MutableList<Message>, var users: List<UserCarnet>,var  viewModel: ProfileViewModel,var likeVisible: Boolean):RecyclerView.Adapter<BirthdayAdapter.BirthDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_birthday_list, parent, false)
        return BirthDayViewHolder(view)
    }

    fun updateList(newlist: MutableList<Message>){
        list.clear()
        list.addAll(newlist)
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n", "LogNotTimber")
    override fun onBindViewHolder(holder: BirthDayViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val sender = users.filter {
            it.email == list[position].sender
        }

        val receiver = users.filter {
            it.email == list[position].receiver
        }

        holder.itemView.sender.text = sender[0].name
        //holder.itemView.receiver.text = "   Receiver"
        holder.itemView.receiver.text = "   ${receiver[0].name}"
        holder.itemView.message.text = list[position].message
        if(position == list.size-1){
            fader(holder.itemView.message_container)
        }
        if (list[position].like!!){
            holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_favorite_border_green)
        }
        holder.itemView.like_image.isEnabled = likeVisible
        holder.itemView.like_image.setOnClickListener {
            fader(holder.itemView.like_image)
            viewModel.likeMessage(list[position].sender!!, list[position].id!!, list[position].receiver!!)
            holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_favorite_border_green)
        }
        holder.itemView.user_photo.apply {
            Glide.with(this).load(sender[0].carnetPhoto).into(this)
        }
        holder.itemView.date_message.text = list[position].date
    }

    private fun fader(star: View) {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    inner class  BirthDayViewHolder(view:View):RecyclerView.ViewHolder(view){
    }
}