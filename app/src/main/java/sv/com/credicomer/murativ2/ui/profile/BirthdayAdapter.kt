package sv.com.credicomer.murativ2.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sv.com.credicomer.murativ2.R

class BirthdayAdapter():RecyclerView.Adapter<BirthdayAdapter.BirthDayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirthDayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_birthday_list, parent, false)

        return BirthDayViewHolder(view)
    }

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: BirthDayViewHolder, position: Int) {
    }

    inner class  BirthDayViewHolder(view:View):RecyclerView.ViewHolder(view){
    }
}