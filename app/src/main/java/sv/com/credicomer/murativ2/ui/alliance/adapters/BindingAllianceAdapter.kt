package sv.com.credicomer.murativ2.ui.alliance.adapters

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.alliance.fragments.AllianceFragmentDirections

@BindingAdapter(value = ["setOnclickCategory"])
fun LinearLayout.setOnclickCategory(category:String){


    setOnClickListener{

        findNavController().navigate(AllianceFragmentDirections.actionNavAlliaToEstablishmentsFragment(category))

    }

}

@BindingAdapter(value = ["setOnclickCategoryTxt"])
fun TextView.setOnclickCategoryTxt(category:String){


    setOnClickListener{

        findNavController().navigate(AllianceFragmentDirections.actionNavAlliaToEstablishmentsFragment(category))

    }

}

@BindingAdapter("setImage")
fun ImageView.setImage(category:String){
    when(category){
        "Clinica" -> setImageResource(R.drawable.ic_medicine)
        "sports"-> setImageResource(R.drawable.ic_alliance_sport)
        "Diversion" -> setImageResource(R.drawable.ic_gamepad)
        "Comida" -> setImageResource(R.drawable.ic_burger)
        "Salud" -> setImageResource(R.drawable.ic_stethoscope)
        "Belleza"->setImageResource(R.drawable.ic_hair_dryer)
        "Educacion"->setImageResource(R.drawable.ic_newspaper)
        "Hogar"->setImageResource(R.drawable.ic_double_bed)
        "Moda"->setImageResource(R.drawable.ic_008_discount)
    }

    setOnClickListener{

        findNavController().navigate(AllianceFragmentDirections.actionNavAlliaToEstablishmentsFragment(category))

    }

}


