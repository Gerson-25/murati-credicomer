package sv.com.credicomer.murativ2.ui.profile.view.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("setProfileImage2")
fun ImageView.setProfileImage2(imageUrl:String){
    Glide.with(this).load(imageUrl).centerCrop().into(this)
}