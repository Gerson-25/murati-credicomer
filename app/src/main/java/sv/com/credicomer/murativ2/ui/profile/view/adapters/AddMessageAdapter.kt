package sv.com.credicomer.murativ2.ui.profile.view.adapters

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.graphics.toColor
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.mikhaellopez.circularimageview.CircularImageView
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet
import sv.com.credicomer.murativ2.ui.profile.view.fragments.OnItemListClickListener

class AddMessageAdapter(
    private val values: List<UserCarnet>
) : RecyclerView.Adapter<AddMessageAdapter.ViewHolder>() {


    private lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_users_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.setIsRecyclable(false)
        holder.idView.text = item.name
        holder.contentView.text = item.email
        Glide.with(view).load(item.carnetPhoto).centerCrop().into(holder.userPhoto)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)
        val userPhoto: CircularImageView =  view.findViewById(R.id.user_photo)
        val userItemContainer: LinearLayout = view.findViewById(R.id.user_item_container)


        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}
