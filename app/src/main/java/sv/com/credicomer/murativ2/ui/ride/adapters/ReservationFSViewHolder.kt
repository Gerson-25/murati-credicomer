package sv.com.credicomer.murativ2.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowReservationBinding
import sv.com.credicomer.murativ2.ui.ride.models.ReservationFS
import sv.com.credicomer.murativ2.ui.ride.viewModels.ReservationFSViewModel

class ReservationFSViewHolder (val binding: ListRowReservationBinding) :
    RecyclerView.ViewHolder(binding.root )   {



    fun bind(item: ReservationFS, viewModel: ReservationFSViewModel) {

        binding.reservation = item
        binding.viewModel = viewModel
        binding.executePendingBindings()

    }


}