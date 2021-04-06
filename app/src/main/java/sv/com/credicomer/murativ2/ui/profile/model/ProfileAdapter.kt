package sv.com.credicomer.murativ2.ui.profile.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_acknowledge.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.profile.fragments.ProfileFragmentDirections

class ProfileAdapter(val acknowledge: List<Acknowledge>, val fragContext:Context):RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){

    lateinit var navController: NavController
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_acknowledge, parent,false)
        return ProfileViewHolder(view)
    }

    override fun getItemCount() = 9

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        if (position+1 <= acknowledge.size)
        {
            holder.itemView.achievement_image.setImageResource(R.drawable.ic_trophy)
            holder.itemView.achievement_image.background = fragContext.resources.getDrawable(R.drawable.dialog_round)
            holder.itemView.achievement_image.setOnClickListener {
                navController = Navigation.findNavController(it)
                navController.navigate(
                    ProfileFragmentDirections.actionProfileFragment2ToAchievementFragment(
                        acknowledge[position]
                    )
                )
            }
        }
        else{
            holder.itemView.achievement_image.setOnClickListener {
                Toast.makeText(fragContext, "Logro Bloqueado!", Toast.LENGTH_SHORT).show()
            }
        }

        holder.setIsRecyclable(false)

        /*if (position == 0){
            holder.itemView.achievement_image.setImageResource(R.drawable.ic_trophy)
            holder.itemView.achievement_image.background = fragContext.resources.getDrawable(R.drawable.dialog_round)
        }*/

    }

    inner class ProfileViewHolder(view: View):RecyclerView.ViewHolder(view)

}

