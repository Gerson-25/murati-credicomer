package sv.com.credicomer.murativ2.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.ride.getDateJoda
import sv.com.credicomer.murativ2.ui.roomsv2.models.*
import sv.com.credicomer.murativ2.ui.roomsv2.viewModels.RoomDetailViewModel
import timber.log.Timber


@SuppressLint("ResourceType", "LogNotTimber")
@BindingAdapter(value = ["validateTime","interval","resultList", "container", "statusIcon", "viewModel", "lifecycle", "user"])
fun CheckBox.validateTime(roomDetail: RoomDetail, roomItem: ListRoomItem, roomWrapper:RoomResultWrapper, container: LinearLayout, icon: ImageView, viewModel: RoomDetailViewModel,lifecycleRegistry: LifecycleOwner, user:String){

    roomDetail.roomDetail!!.forEach{
        if(roomItem.schedule == it.key){
            if(it.value.email == user){
                viewModel.increaseReservationCounter("sumar")
            }
            visibility = View.GONE
            isEnabled = false
            isChecked = false
            roomItem.isChecked = false
        }
        else{
            val horario = roomItem.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                visibility = View.GONE
                isEnabled = false
                isChecked = false
                roomItem.isChecked = false
            }
        }
    }

    viewModel.reservationCounter.observe(lifecycleRegistry, Observer {
        if (it >= 8){
            Toast.makeText(context, "Has alcanzado el maximo de reservaciones", Toast.LENGTH_SHORT).show()
            if(!isChecked) {
                isEnabled = false
            }
        }
        else{
            if(!isChecked) {
                isEnabled = true
            }
        }
    })

    setOnCheckedChangeListener { compoundButton, b ->
        if (b){
            viewModel.increaseReservationCounter("sumar")
        }
        else
        {
            viewModel.increaseReservationCounter("restar")
        }
    }
    setOnClickListener{
        viewModel.reservationCounter.observe(lifecycleRegistry, Observer {
            //isChecked = it <= 4
        })
                if (isEnabled){
                    if (isChecked){
                        container.background = resources.getDrawable(R.drawable.list_room_schedule_selected)
                        icon.apply {
                            setImageResource(R.drawable.ic_passkey_selected)
                            background = resources.getDrawable(R.drawable.bg_room_items)
                        }
                        val list=roomItem.schedule?.split("-")
                        val date= getDateJoda()
                        roomWrapper.resultList.add(RoomResult(roomItem.schedule, mutableListOf(list!![0],list[1]),"$date|${list[0]}"))
                    }else{
                        container.background = resources.getDrawable(R.drawable.list_room_schedule)
                        icon.apply {
                            setImageResource(R.drawable.ic_passkey_inactive)
                            background = resources.getDrawable(R.drawable.bg_room_items_active)
                        }
                        val result =roomWrapper.resultList.filter {it.hour!=roomItem.schedule }.toMutableList()
                        Timber.d("BINDING-FILTERED %s","${roomWrapper.resultList}")
                        roomWrapper.resultList.clear()
                        roomWrapper.resultList.addAll(result)
                        Timber.d("BINDING-RESULT %s","${roomWrapper.resultList}")

                    }
                }
       // Toast.makeText(context,"the interval is-> $interval",Toast.LENGTH_LONG).show()
    }

}

@BindingAdapter(value = ["checkAvailable","interval", "textColor", "user"])
fun TextView.checkAvailable(roomDetail:RoomDetail,interval: ListRoomItem, color:Int, user: String){

    roomDetail.roomDetail!!.forEach{
        if(interval.schedule== it.key){
            if(it.value.email != user){
                setTextColor(color)
            }

        }else{
            val horario = interval.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                if(it.value.email != user){
                    setTextColor(color)
                }
            }

        }
    }

}


@BindingAdapter(value = ["paintUnavailable","interval", "currentUser", "roomWrapper"])
fun LinearLayout.paintUnavailable(roomDetail:RoomDetail,interval: ListRoomItem, user:String, roomWrapper:RoomResultWrapper ){

  roomDetail.roomDetail!!.forEach{
      if(interval.schedule == it.key){
          roomWrapper.resultList.clear()
          if(it.value.email == user ){
              background = resources.getDrawable(R.drawable.list_room_schedule_active)
          }

      }else{

              val horario = interval.schedule!!.split("-")
              val reservationData = it.key.split("-")
              if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                  roomWrapper.resultList.clear()
                  Timber.d("HORARIO_0 %s", "${horario[0]>=reservationData[0] && horario[1]<=reservationData[1]}")
                  if(it.value.email == user ){
                      background = resources.getDrawable(R.drawable.list_room_schedule_active)
                  }
              }
      }
  }

}


@BindingAdapter(value = ["userEmail","interval", "currentUser", "color"])
fun TextView.userEmail(roomDetail:RoomDetail,interval: ListRoomItem, user: String, color: Int){
    roomDetail.roomDetail!!.forEach{
        if(interval.schedule== it.key){
            if(it.value.email != user ){
                setTextColor(color)
                text=it.value.email
            }
            else{
                text = "Mi Reserva"
            }

        }
        else{
            val horario = interval.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                if(it.value.email != user ){
                    setTextColor(color)
                    text=it.value.email
                }
                else{
                    text = "Mi Reserva"
                }
            }
        }
    }
}

@BindingAdapter(value = ["interval", "items", "user", "viewModel"])
fun ImageView.setVisible(roomDetail:RoomDetail,interval: ListRoomItem, user: String, viewModel: RoomDetailViewModel ){

    roomDetail.roomDetail!!.forEach{

        if(interval.schedule== it.key){

            if(it.value.email == user){

                visibility = View.VISIBLE
                setOnClickListener {view->


                }
            }

        }
        else{
            val horario = interval.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                if(it.value.email == user){
                    visibility = View.VISIBLE
                    setOnClickListener {view->

                    }
                }
            }
        }
    }
}

@BindingAdapter("setRoomImage")
fun ImageView.setRoomImage(roomImage:List<String>){
    Glide.with(this).load(roomImage[0]).into(this)
}

@BindingAdapter(value = ["setLock", "items", "user"])
fun ImageView.setLock(roomDetail:RoomDetail,interval: ListRoomItem, user: String){

    roomDetail.roomDetail!!.forEach{
        if(interval.schedule== it.key){
            if(it.value.email != user){
                setImageResource(R.drawable.ic_lock)
            }
            else
            {
                setBackgroundResource(R.drawable.bg_room_items_mine)
                setImageResource(R.drawable.ic_passkey)
            }
        }
        else{
            val horario = interval.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                if(it.value.email != user){
                    setImageResource(R.drawable.ic_lock)
                }
                else
                {
                    setBackgroundResource(R.drawable.bg_room_items_mine)
                    setImageResource(R.drawable.ic_passkey)
                }
            }
        }
    }
}

@BindingAdapter("setProfileImage")
fun ImageView.setProfileImage(imageUrl:String){
    Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_005_dracula).into(this)
}

