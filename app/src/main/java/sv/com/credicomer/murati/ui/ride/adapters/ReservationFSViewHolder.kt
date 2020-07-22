package sv.com.credicomer.murati.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowReservationBinding
import sv.com.credicomer.murati.ui.ride.models.ReservationFS
import sv.com.credicomer.murati.ui.ride.viewModels.ReservationFSViewModel

class ReservationFSViewHolder (val binding: ListRowReservationBinding) :
    RecyclerView.ViewHolder(binding.root )   {



    fun bind(item: ReservationFS, viewModel: ReservationFSViewModel) {

        binding.reservation = item
        binding.viewModel = viewModel
        binding.executePendingBindings()

    }


}