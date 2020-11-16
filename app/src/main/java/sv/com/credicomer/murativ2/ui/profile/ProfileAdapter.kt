package sv.com.credicomer.murativ2.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_acknowledge.view.*
import sv.com.credicomer.murativ2.R

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
    }

    inner class ProfileViewHolder(view: View):RecyclerView.ViewHolder(view)

}

