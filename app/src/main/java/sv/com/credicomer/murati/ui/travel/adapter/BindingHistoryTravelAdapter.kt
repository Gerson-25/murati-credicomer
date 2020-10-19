package sv.com.credicomer.murati.ui.travel.adapter

import android.util.Log
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayout
import sv.com.credicomer.murati.ui.travel.fragments.HomeFragmentDirections
import sv.com.credicomer.murati.ui.travel.models.Travel
import sv.com.credicomer.murati.ui.travel.viewModel.HomeViewModel


@BindingAdapter(value = ["historyItem","viewModelTravel"])
fun FrameLayout.historyItem(item:Travel,viewModel: HomeViewModel){
    setOnClickListener{
        item.let {travel ->
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavHomeTravel(travel.travelId!!,
                travel.dateRegister!!,travel.active))
        }
    }
}