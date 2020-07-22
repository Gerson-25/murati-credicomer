package sv.com.credicomer.murati.ui.travel.adapter

import android.widget.FrameLayout
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.travel.fragments.HomeTravelFragmentDirections
import sv.com.credicomer.murati.ui.travel.models.Record


@BindingAdapter(value = ["clickItem","id"])
fun FrameLayout.clickItem(item:Record, id:String){
    setOnClickListener {
        item.let {
            findNavController().navigate(HomeTravelFragmentDirections.actionNavHomeTravelToNavDetailrecord(it, id))
        }
    }

}

@BindingAdapter(value = ["setImageCategory"])
fun ImageView.setImageCategory(item: Record){

    item.let {
        when(item.recordCategory){
            "0" ->{
                setImageResource(R.drawable.ic_cat_food)
            }
            "1" ->{
                setImageResource(R.drawable.ic_cat_car)
            }
            "2" ->{
                setImageResource(R.drawable.ic_cat_hotel)
            }
            "3" ->{
                setImageResource(R.drawable.ic_cat_other)
            }
        }


    }
}