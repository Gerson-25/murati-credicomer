package sv.com.credicomer.murativ2.ui.travel.adapter

import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import sv.com.credicomer.murativ2.ui.travel.fragments.HomeFragmentDirections
import sv.com.credicomer.murativ2.ui.travel.models.Travel
import sv.com.credicomer.murativ2.ui.travel.viewModel.HomeViewModel


@BindingAdapter(value = ["historyItem","viewModelTravel"])
fun FrameLayout.historyItem(item:Travel,viewModel: HomeViewModel){
    setOnClickListener{
        item.let {travel ->
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavHomeTravel(travel.travelId!!,
                travel.dateRegister!!,travel.active))
        }
    }
}