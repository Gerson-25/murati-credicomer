package sv.com.credicomer.murati.ui.roomsv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_reservation.view.text_schedule
import kotlinx.android.synthetic.main.item_room_reservation.view.*
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.databinding.FragmentMyReservationsBinding
import sv.com.credicomer.murati.ui.roomsv2.fragments.MyReservationsFragment
import sv.com.credicomer.murati.ui.roomsv2.fragments.MyReservationsFragmentDirections
import sv.com.credicomer.murati.ui.roomsv2.models.*

class ReservationsAdapter(var reservations:MutableList<Reservations>, var rooms:MutableList<Room>, val context: MyReservationsFragment, var date:String):RecyclerView.Adapter<ReservationsAdapter.ReservationsVH>() {

    lateinit var navController: NavController

    fun updateReservations(newList: MutableList<Reservations>){
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
    lateinit var delete: (String, String, String) -> (Unit)
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


        rooms.forEach { room->
            if(room.roomName == reservations[position].roomName){
                holder.itemView.image_room_picture.let {
                    Glide.with(it).load(room.roomImages!![0]).into(it)
                }
            }
        }
        holder.itemView.text_schedule.text = reservations[position].schedule
        holder.itemView.text_roomName.text = reservations[position].roomName!!.capitalize()
        holder.itemView.floating_btn_delete.setOnClickListener {
                delete(reservations[position].schedule!!, reservations[position].roomId!!, reservations[position].date!!)
        }
        holder.itemView.setOnClickListener {
            rooms.forEach {room ->
                if (room.roomName == reservations[position].roomName)
                {
                    navController.navigate(MyReservationsFragmentDirections.actionMyReservationsFragmentToRoomDetailFragment(room,date))
                }
            }
        }
    }

}

