package sv.com.credicomer.murati.ui.alliance.adapters

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.alliance.fragments.AllianceFragmentDirections

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
        "Clinica" -> setImageResource(R.drawable.ic_alliance_clinic)
        "sports"-> setImageResource(R.drawable.ic_alliance_sport)
        "Diversion" -> setImageResource(R.drawable.ic_diversion)
        "Comida" -> setImageResource(R.drawable.ic_alliance_food)
        "Salud" -> setImageResource(R.drawable.ic_alliance_health)
        "Belleza"->setImageResource(R.drawable.ic_beauty)
        "Educacion"->setImageResource(R.drawable.ic_educacion)
        "Hogar"->setImageResource(R.drawable.ic_hogar)
        "Moda"->setImageResource(R.drawable.ic_moda)
    }

    setOnClickListener{

        findNavController().navigate(AllianceFragmentDirections.actionNavAlliaToEstablishmentsFragment(category))

    }

}


