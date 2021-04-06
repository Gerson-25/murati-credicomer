package sv.com.credicomer.murativ2.ui.ride.adapters

import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.databinding.ListRowReservationHeaderBinding


class ReservationFSHeaderViewHolder (val binding: ListRowReservationHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)   {



    fun bind() {


        binding.executePendingBindings()

    }


}