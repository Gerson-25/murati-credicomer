package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import sv.com.credicomer.murativ2.ui.roomsv2.fragments.RoomsHomeFragmentDirections
import sv.com.credicomer.murativ2.ui.roomsv2.models.Room


@BindingAdapter(value = ["roomImage"])
fun ImageView.roomImage(room: Room) {

    room.let {

        Glide.with(this).load(it.roomImages?.get(0)).centerCrop().into(this)
    }


}

/*@BindingAdapter(value=["roomDetail"])
fun CardView.roomDetail(room:Room){

    room.let {
        findNavController().navigate(RoomsHomeFragmentDirections.actionNavRooms2ToRoomDetailFragment(it))
    }

}*/

@BindingAdapter(value = ["roomDetail","date"])
fun ConstraintLayout.roomDetail(room: Room,date:String) {

    setOnClickListener {
        findNavController().navigate(
            RoomsHomeFragmentDirections.actionNavRooms2ToRoomDetailFragment(
                room
            )
        )
    }

}

@SuppressLint("DefaultLocale")
@BindingAdapter(value = ["capitalize"])
fun TextView.capitalize(room: Room) {


    room.let {

        text = room.roomName?.capitalize()
    }

}

/*@BindingAdapter(value = ["setEquipment"])
fun TextView.setEquipment(room: Room) {

    var chain = ""

    room.equipment?.forEachIndexed { index, item ->

        chain += if (index == room.equipment!!.size - 1) {
            item
        } else {
            "$item,"
        }

    }

    text = chain

}*/

@BindingAdapter(value = ["setIcons"])
fun ImageView.setIcons(room: Room) {
    visibility = View.GONE
    //Log.d("TAG", "room equipment: ${room.equipment} room name: ${contentDescription}")
    room.equipment!!.forEach {
        if (contentDescription == it){
            visibility = View.VISIBLE
        }
    }
}