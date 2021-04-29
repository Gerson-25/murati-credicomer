package sv.com.credicomer.murativ2.ui.profile.view.adapters

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import kotlinx.android.synthetic.main.item_birthday_list.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentAchievementListBinding
import sv.com.credicomer.murativ2.databinding.ItemBirthdayListBinding
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.viewmodel.ProfileViewModel

class RecognitionsAdapter(var list:MutableList<Recognition>, var users: List<UserCarnet>, var  viewModel: ProfileViewModel, var myEmail:String):RecyclerView.Adapter<RecognitionsAdapter.BirthDayViewHolder>() {

    private lateinit var recognitionHandler:RecognitionHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemBirthdayListBinding>(inflater, R.layout.item_birthday_list, parent, false)
        return BirthDayViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BirthDayViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        recognitionHandler = RecognitionHandler()
        var headerText = holder.itemView.sender
        val senderData = recognitionHandler.getSenderData(list[position].sender!!, users)
        val receiverData = recognitionHandler.getSenderData(list[position].receiver!![0], users)
        headerText.text = "${senderData.name} ha enviado un reconocimiento para ${receiverData.name} y ${list[position].receiver!!.size-1} personas mas"
        holder.itemView.receiver.text = receiverData.name
        holder.itemView.message.text = list[position].message
        holder.itemView.likes_counter.text = list[position].like!!.size.toString()
        holder.itemView.header_container.setOnClickListener {
            headerText.text = "${senderData.name} ha enviado un reconocimiento para"
            list[position].receiver!!.forEach { user ->
                val email = recognitionHandler.getSenderData(user, users)

                if (user == list[position].receiver!![list[position].receiver!!.size-1]){
                    headerText.text = "${headerText.text} y ${email.name}"
                }
                else{
                    headerText.text = "${headerText.text} ${email.name}, "
                }
            }
        }
        if (list[position].like!!.contains(myEmail)){
            holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
        else{
            holder.itemView.like_image.setOnClickListener {
                fader(holder.itemView.like_image)
                if (list[position].like!!.isEmpty()){
                    viewModel.likeMessage(list[position].id!!, listOf(myEmail))
                }
                else{
                    val newLikesList = list[position].like!!.toMutableList()
                    newLikesList.add(myEmail)
                    viewModel.likeMessage(list[position].id!!, newLikesList)
                }

                holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
        }

        holder.itemView.user_photo.apply {
            Glide.with(this).load(senderData.carnetPhoto).into(this)
        }
        holder.itemView.date_message.text = list[position].date
    }

    private fun fader(star: View) {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    inner class  BirthDayViewHolder(var view: ItemBirthdayListBinding):RecyclerView.ViewHolder(view.root){
    }
}