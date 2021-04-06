package sv.com.credicomer.murativ2.ui.alliance.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.alliance.fragments.AllianceFragmentDirections
import sv.com.credicomer.murativ2.ui.alliance.fragments.PromotionsFragmentDirections
import sv.com.credicomer.murativ2.ui.alliance.models.PromotionFS
import java.text.SimpleDateFormat


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
@BindingAdapter(value = ["parseDate"])
fun TextView.parseDate(item:PromotionFS){

    item.let {

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        val date=sdf.format(item.date?.toDate()).toString()

        text=date


    }

}

@BindingAdapter(value=["displayGlideImage"])
fun ImageView.displayGlideImage(item: PromotionFS){

    Glide.with(this).load(item.image).into(this)

}

@BindingAdapter(value=["circularEstablishmentImage"])
fun CircleImageView.circularEstablishmentImage(item: PromotionFS){

    Glide.with(this).load(item.establishment_image).into(this)


}


@BindingAdapter(value = ["onClickPromotion"])
fun LinearLayout.onclickPromotion(item:PromotionFS){


    setOnClickListener {

           // Toast.makeText(context,"clicked on -> ${item.promotion_name}",Toast.LENGTH_LONG).show()
        if (findNavController().currentDestination==findNavController().graph.findNode(R.id.nav_alliance)){
        findNavController().navigate(AllianceFragmentDirections.actionNavAlliaToPromotionDetailFragment(item))
        }else{
            findNavController().navigate(PromotionsFragmentDirections.actionPromotionsFragmentToPromotionDetailFragment(item))

        }


    }

}



