package sv.com.credicomer.murati.ui.travel.adapter

import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import sv.com.credicomer.murati.ui.travel.fragments.HomeFragmentDirections
import sv.com.credicomer.murati.ui.travel.models.Travel
import sv.com.credicomer.murati.ui.travel.viewModel.HomeViewModel


@BindingAdapter(value = ["historyItem","viewModelTravel"])
fun FrameLayout.historyItem(item:Travel,viewModel: HomeViewModel){
    setOnClickListener{
        item.let {

            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavHomeTravel(item.travelId.toString(),
                it.dateRegister.toString(),it.active))
        }
    }
}