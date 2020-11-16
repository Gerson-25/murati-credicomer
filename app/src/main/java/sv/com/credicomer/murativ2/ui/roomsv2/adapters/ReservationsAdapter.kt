package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_reservation.view.text_schedule
import kotlinx.android.synthetic.main.item_room_reservation.view.*
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.databinding.FragmentMyReservationsBinding
import sv.com.credicomer.murativ2.ui.roomsv2.fragments.MyReservationsFragment
import sv.com.credicomer.murativ2.ui.roomsv2.fragments.MyReservationsFragmentDirections
import sv.com.credicomer.murativ2.ui.roomsv2.models.*

class ReservationsAdapter(var reservations:MutableList<ReservationList>, var rooms:MutableList<Room>, val context: MyReservationsFragment, var date:String):RecyclerView.Adapter<ReservationsAdapter.ReservationsVH>() {

    lateinit var navController: NavController

    fun updateReservations(newList: MutableList<ReservationList>){
        reservations.clear()
        reservations.addAll(newList)
        notifyDataSetChanged()
        //val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        //reservations.sortBy { LocalDate.parse(it.date, dateTimeFormatter)}
    }

    fun updateRooms(newList: MutableList<Room>){
        rooms.clear()
        rooms.addAll(newList)
        notifyDataSetChanged()
    }

    fun getDate(newDate:String){
        date = newDate
    }
    lateinit var delete: (String, String) -> (Unit)
    lateinit var nav: () -> (Unit)

    class ReservationsVH(item: View):RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsVH {
        navController = findNavController(context)
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_room_reservation,parent,false)

        return ReservationsVH(view)
    }

    override fun getItemCount() = reservations.size

    override fun onBindViewHolder(holder: ReservationsVH, position: Int) {
        var roomId = ""

        rooms.forEach {
            if (it.roomName == reservations[position].room){
                roomId = it.roomId!!
            }
        }
        holder.itemView.date.text = reservations[position].date
        holder.itemView.text_schedule.text = "${reservations[position].start} - ${reservations[position].end}"
        holder.itemView.text_roomName.text = reservations[position].room.toUpperCase()
        holder.itemView.floating_btn_delete.setOnClickListener {
                delete(reservations[position].id, roomId)
        }
        holder.itemView.setOnClickListener {
            rooms.forEach {room ->
                if (room.roomName == reservations[position].room)
                {
                    navController.navigate(MyReservationsFragmentDirections.actionMyReservationsFragmentToRoomDetailFragment(room))
                }
            }
        }
    }

}

