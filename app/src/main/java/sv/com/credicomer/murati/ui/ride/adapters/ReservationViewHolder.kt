package sv.com.credicomer.murati.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowCardRsvBinding
import sv.com.credicomer.murati.ui.ride.models.Reservation
import sv.com.credicomer.murati.ui.ride.viewModels.ReservationViewModel


class ReservationViewHolder(val binding: ListRowCardRsvBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Reservation, viewModel: ReservationViewModel) {

        binding.reservation = item
        binding.viewModel = viewModel
        binding.executePendingBindings()

    }


}