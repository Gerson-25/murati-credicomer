package sv.com.credicomer.murativ2.ui.profile

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_birthday_list.view.*
import sv.com.credicomer.murativ2.R

class BirthdayAdapter(var list:MutableList<Acknowledge>):RecyclerView.Adapter<BirthdayAdapter.BirthDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_birthday_list, parent, false)
        return BirthDayViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BirthDayViewHolder, position: Int) {
        holder.itemView.like_image.setOnClickListener {
            fader(holder.itemView.like_image)
            holder.itemView.like_image.setImageResource(R.drawable.ic_baseline_star_border_green)
        }
    }

    private fun fader(star: ImageView) {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    inner class  BirthDayViewHolder(view:View):RecyclerView.ViewHolder(view){

    }
}