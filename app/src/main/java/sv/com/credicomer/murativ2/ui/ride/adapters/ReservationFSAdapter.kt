package sv.com.credicomer.murativ2.ui.ride.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.ListRowReservationBinding
import sv.com.credicomer.murativ2.databinding.ListRowReservationHeaderBinding
import sv.com.credicomer.murativ2.databinding.ListRowReservationVictorBinding
import sv.com.credicomer.murativ2.ui.ride.*
import sv.com.credicomer.murativ2.ui.ride.models.ReservationFS
import sv.com.credicomer.murativ2.ui.ride.viewModels.ReservationFSViewModel


class ReservationFSAdapter(var viewModel: ReservationFSViewModel, var context:Context) :
    ListAdapter<AdapterFSItem<ReservationFS>, RecyclerView.ViewHolder>(ReservationFSDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListRowReservationBinding.inflate(layoutInflater, parent, false)
        val headerBinding = ListRowReservationHeaderBinding.inflate(layoutInflater, parent, false)
        val headerBindingVictor= ListRowReservationVictorBinding.inflate(layoutInflater, parent, false)

        return when (viewType) {
            TYPE_ITEM -> {
                ReservationFSViewHolder(binding)
            }
            TYPE_VICTOR_HEADER->{
                VictorViewHolder(headerBindingVictor)
            }
            TYPE_MORNING_HEADER -> {
                ReservationFSHeaderViewHolder(headerBinding)
            }
            TYPE_EVENING_HEADER -> {
                ReservationFSHeaderViewHolder(headerBinding)
            }
            TYPE_AFTERNOON_HEADER -> {
                ReservationFSHeaderViewHolder(headerBinding)
            }
            else -> {
                throw ClassCastException("Unknown viewType $viewType")
            }

        }

    }

    override fun getItemViewType(position: Int) = getItem(position).viewType


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position).value
        val type = getItem(position).viewType

        when (holder) {

            is ReservationFSHeaderViewHolder -> {
                //  holder.bind()
                when (type) {
                    TYPE_MORNING_HEADER -> {
                        holder.binding.txtViewHeader.text = context.getString(R.string.txt_view_header_morning)
                    }
                    TYPE_EVENING_HEADER -> {
                        holder.binding.txtViewHeader.text = context.getString(R.string.txt_view_header_middletime)
                    }
                    TYPE_AFTERNOON_HEADER -> {
                        holder.binding.txtViewHeader.text = context.getString(R.string.txt_view_header_afternoon)
                    }
                }
            }
            is ReservationFSViewHolder -> {
                item?.let { holder.bind(it, viewModel) }

            }


        }
    }
}


class ReservationFSDiffCallback : DiffUtil.ItemCallback<AdapterFSItem<ReservationFS>>() {
    override fun areItemsTheSame(
        oldItem: AdapterFSItem<ReservationFS>,
        newItem: AdapterFSItem<ReservationFS>
    ): Boolean {
        return oldItem.value?.id === newItem.value?.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: AdapterFSItem<ReservationFS>,
        newItem: AdapterFSItem<ReservationFS>
    ): Boolean {
        return oldItem == newItem
    }
}

class LottieListener(val lottieListener: () -> Unit) {

    fun showLottie() = lottieListener()
}
