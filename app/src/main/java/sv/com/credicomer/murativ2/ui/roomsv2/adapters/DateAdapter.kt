package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.date_row_item.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.DateRowItemBinding
import sv.com.credicomer.murativ2.ui.roomsv2.models.Date
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel

class DateAdapter(var dateList:ArrayList<Date>, var viewModel: RoomDetailViewModel, var roomId:String)
    :androidx.recyclerview.widget.ListAdapter<Date, DateAdapter.DateViewHolder>(DateDiffCallBack()) {

    inner class DateViewHolder(binding: DateRowItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(dates:Date){
            binding.dates = dates
        }

    }

    lateinit var binding: DateRowItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.date_row_item, parent,false)
        return DateViewHolder(binding)
    }

    class DateDiffCallBack:DiffUtil.ItemCallback<Date>(){
        override fun areItemsTheSame(oldItem: Date, newItem: Date): Boolean {
            return oldItem.dayOfMonth == newItem.dayOfMonth
        }

        override fun areContentsTheSame(oldItem: Date, newItem: Date): Boolean {
            return oldItem == newItem

        }

    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val dateFormatted = dateList[position].dayOfMonth + "-" + dateList[position].month + "-" + dateList[position].year
        holder.itemView.date_item_container.setOnClickListener {
            viewModel.selectDay(position+1, dateList[position].date, dateFormatted)
            viewModel.getRoomReservation(roomId, dateFormatted)
        }
        val item = getItem(position)
        holder.bind(item)
        /*holder.itemView.textview_day_of_week.text = dateList[position].dayOfWeek
        holder.itemView.textview_day_of_month.text = dateList[position].dayOfMonth
        if (dateList[position].isSelected){
        }
        holder.itemView.date_item_container.setOnClickListener {
            holder.itemView.date_item_container.setBackgroundColor(R.color.primaryActionColor)
            holder.itemView.textview_day_of_week.setTextColor(R.color.white)
            holder.itemView.textview_day_of_month.setTextColor(R.color.white)
            //viewModel.selectDay(position+1)
        }*/
    }
}