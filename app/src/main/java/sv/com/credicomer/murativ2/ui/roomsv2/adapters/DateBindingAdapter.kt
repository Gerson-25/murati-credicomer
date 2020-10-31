package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.roomsv2.models.Date

@BindingAdapter("setDateOfWeek")
fun TextView.setDateOfWeek(date:Date){
    when(date.dayOfWeek){
        "Mon" ->  text = "Lun"
        "Tue" ->  text = "Mar"
        "Wed" ->  text = "Mie"
        "Thu" ->  text = "Jue"
        "Fri" ->  text = "Vie"
        "Sat" ->  text = "Sab"
        "Sun" ->  text = "Dom"
    }

    if (date.isSelected){
        setTextColor(resources.getColor(R.color.white))
    }
}

@BindingAdapter("setDateOfMonth")
fun TextView.setDateOfMonth(date:Date){
    text = date.dayOfMonth
    if (date.isSelected){
        setTextColor(resources.getColor(R.color.white))
    }
}

@BindingAdapter("setSelected")
fun LinearLayout.setSelected(date:Date){
    if (date.isSelected){
        background = resources.getDrawable(R.drawable.bg_row_room_reservation)
    }
}