package sv.com.credicomer.murati.ui.profile

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginEnd
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_acknowledge.view.*
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.constants.LOGIN_DIALOG
import sv.com.credicomer.murati.dialogs.LoginDialogFragment

class ProfileAdapter(val acknowledge: List<Acknowledge>, val fragContext:Context):RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){

    lateinit var navController: NavController
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_acknowledge, parent,false)

        return ProfileViewHolder(view)
    }

    override fun getItemCount()=acknowledge.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.itemView.linearlayout_achievement.setOnClickListener {
            navController = Navigation.findNavController(it)
            navController.navigate(ProfileFragmentDirections.actionProfileFragment2ToAchievementFragment())
        }
        /*when(acknowledge[position].type){
            "one"-> holder.itemView.icon.setImageResource(R.drawable.ic_framebadge_one)
            "two"-> holder.itemView.icon.setImageResource(R.drawable.ic_framebadge_two)
            "three"->holder.itemView.icon.setImageResource(R.drawable.ic_framebadge_three)
            "Congrats"->holder.itemView.icon.setImageResource(R.drawable.ic_confetti)
        }*/
    }

    inner class ProfileViewHolder(view: View):RecyclerView.ViewHolder(view)

}

