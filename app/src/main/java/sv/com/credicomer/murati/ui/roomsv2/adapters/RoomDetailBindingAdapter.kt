package sv.com.credicomer.murati.ui.roomsv2.adapters

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.ui.ride.getDateJoda
import sv.com.credicomer.murati.ui.roomsv2.models.*
import sv.com.credicomer.murati.ui.roomsv2.viewModels.RoomDetailViewModel
import timber.log.Timber

@SuppressLint("ResourceType")
@BindingAdapter(value = ["validateTime","interval","resultList", "container", "statusIcon"])
fun CheckBox.validateTime(roomDetail: RoomDetail, roomItem: ListRoomItem, roomWrapper:RoomResultWrapper, container: LinearLayout, icon: ImageView){


    roomDetail.roomDetail!!.forEach{
        if(roomItem.schedule == it.key){
            /*if(it.value.email == user){
            }*/
            visibility = View.GONE
            isEnabled = false
            isChecked = false
            roomItem.isChecked = false
        }
        else{
            val horario = roomItem.schedule!!.split("-")
            val reservationData = it.key.split("-")
            if (horario[0]>=reservationData[0] && horario[1]<=reservationData[1]){
                /*if(it.value.email == user){

                }*/
                visibility = View.GONE
                isEnabled = false
                isChecked = false
                roomItem.isChecked = false
            }
        }
    }

    setOnClickListener{

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
                    val scheduleArray = it.key.split("-")
                    val itemArray = interval.schedule!!.split("-")
                    val date = viewModel._day.value
                    val roomId = viewModel._roomId.value

                    if (scheduleArray[0] == itemArray[0] && scheduleArray[1] == itemArray[1]){
                        viewModel.deleteReservation(it.key, roomId!!, date!!)
                    }
                    else if(scheduleArray[0] == itemArray[0] || scheduleArray[1] == itemArray[1]){
                        if (scheduleArray[0] == itemArray[0]){
                            val intervalList = mutableListOf(scheduleArray[1],itemArray[1])
                            val reservation = RoomResult(scheduleArray[1]+"-"+itemArray[1], intervalList ,"")
                            val roomResult = mutableListOf(reservation)
                            viewModel.deleteReservation(it.key, roomId!!, date!!)
                            viewModel.pushRoomReservation(roomResult, roomId, date)
                        }
                        else{
                            val intervalList = mutableListOf(scheduleArray[0],itemArray[0])
                            val reservation = RoomResult(scheduleArray[0]+"-"+itemArray[0], intervalList ,"")
                            val roomResult = mutableListOf(reservation)
                            viewModel.deleteReservation(it.key, roomId!!, date!!)
                            viewModel.pushRoomReservation(roomResult, roomId, date)
                        }
                    }
                    else{
                        val intervalList1 = mutableListOf(scheduleArray[0],itemArray[0])
                        val intervalList2 = mutableListOf(scheduleArray[1],itemArray[1])
                        val reservation1 = RoomResult(scheduleArray[0]+"-"+itemArray[0], intervalList1 ,"")
                        val reservation2 = RoomResult(scheduleArray[1]+"-"+itemArray[1], intervalList2 ,"")
                        val roomResult = mutableListOf(reservation1, reservation2)
                        viewModel.deleteReservation(it.key, roomId!!, date!!)
                        viewModel.pushRoomReservation(roomResult, roomId, date)
                    }
                    /*viewModel.funPrueba(context)*/
                    //Toast.makeText(context, "schedule: ${it.key}, item: ${roomId}, day: ${date}", Toast.LENGTH_SHORT).show()
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
                        val scheduleArray = it.key.split("-")
                        val itemArray = interval.schedule!!.split("-")
                        val date = viewModel._day.value
                        val roomId = viewModel._roomId.value

                        if (scheduleArray[0] == itemArray[0] && scheduleArray[1] == itemArray[1]){
                            viewModel.deleteReservation(it.key, roomId!!, date!!)
                        }
                        else if(scheduleArray[0] == itemArray[0] || scheduleArray[1] == itemArray[1]){
                            if (scheduleArray[0] == itemArray[0]){
                                val intervalList = mutableListOf(itemArray[1],scheduleArray[1])
                                val reservation = RoomResult(itemArray[1]+"-"+scheduleArray[1], intervalList ,"")
                                val roomResult = mutableListOf(reservation)
                                viewModel.deleteReservation(it.key, roomId!!, date!!)
                                viewModel.pushRoomReservation(roomResult, roomId, date)
                            }
                            else{
                                val intervalList = mutableListOf(scheduleArray[0],itemArray[0])
                                val reservation = RoomResult(scheduleArray[0]+"-"+itemArray[0], intervalList ,"")
                                val roomResult = mutableListOf(reservation)
                                viewModel.deleteReservation(it.key, roomId!!, date!!)
                                viewModel.pushRoomReservation(roomResult, roomId, date)
                            }
                        }
                        else{
                            val intervalList1 = mutableListOf(scheduleArray[0],itemArray[0])
                            val intervalList2 = mutableListOf(itemArray[1], scheduleArray[1])
                            val reservation1 = RoomResult(scheduleArray[0]+"-"+itemArray[0], intervalList1 ,"")
                            val reservation2 = RoomResult(itemArray[1]+"-"+scheduleArray[1], intervalList2 ,"")
                            val roomResult = mutableListOf(reservation1, reservation2)
                            viewModel.deleteReservation(it.key, roomId!!, date!!)
                            viewModel.pushRoomReservation(roomResult, roomId, date)
                        }
                        /*viewModel.funPrueba(context)*/
                        /* Toast.makeText(context, "schedule: ${it.key}, item: ${interval.schedule}", Toast.LENGTH_SHORT).show()*/
                    }
                }
            }
        }
    }

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
