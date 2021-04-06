package sv.com.credicomer.murativ2.ui.alliance.adapters

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import sv.com.credicomer.murativ2.ui.alliance.fragments.EstablishmentsFragmentDirections
import sv.com.credicomer.murativ2.ui.alliance.models.EstablishmentFS

@BindingAdapter(value = ["establishmentItem","category"])
fun ConstraintLayout.establishmentItem(item: EstablishmentFS,category: String){
    setOnClickListener{


        item.let {
            findNavController().navigate(EstablishmentsFragmentDirections
                .actionEstablishmentsFragmentToPromotionsFragment(it,category
            ))
        }
    }
}



@BindingAdapter(value =["displayEstablishmentImage"])
fun ImageView.displayEstablishmentImage(item: EstablishmentFS){


    Glide.with(this).load(item.establishment_image).into(this)

}

