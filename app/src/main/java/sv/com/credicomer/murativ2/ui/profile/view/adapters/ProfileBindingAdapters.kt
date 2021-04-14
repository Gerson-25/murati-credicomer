package sv.com.credicomer.murativ2.ui.profile.view.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setProfileImage")
fun ImageView.setProfileImage(imageUrl:String){
    Glide.with(this).load(imageUrl).centerCrop().into(this)
}