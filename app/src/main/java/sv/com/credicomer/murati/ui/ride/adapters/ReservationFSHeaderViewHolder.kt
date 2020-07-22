package sv.com.credicomer.murati.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murati.databinding.ListRowReservationHeaderBinding


class ReservationFSHeaderViewHolder (val binding: ListRowReservationHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)   {



    fun bind() {


        binding.executePendingBindings()

    }


}