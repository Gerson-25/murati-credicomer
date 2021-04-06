package sv.com.credicomer.murativ2.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowCardRsvBinding
import sv.com.credicomer.murativ2.ui.ride.models.Reservation
import sv.com.credicomer.murativ2.ui.ride.viewModels.ReservationViewModel


class ReservationViewHolder(val binding: ListRowCardRsvBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Reservation, viewModel: ReservationViewModel) {

        binding.reservation = item
        binding.viewModel = viewModel
        binding.executePendingBindings()

    }


}