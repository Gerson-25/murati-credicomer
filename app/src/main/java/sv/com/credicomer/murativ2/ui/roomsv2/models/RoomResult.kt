package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class RoomResult(var hour:String?="",var interval:MutableList<String>?= mutableListOf(),var sortParam:String?="")

data class RoomResultWrapper(var resultList: MutableList<RoomResult>)

data class ListRoomItem(var schedule:String?="",var isChecked:Boolean?=false)

@Parcelize
data class RoomDetail(var roomDetail:MutableMap<String, RoomReservation>?= mutableMapOf()):Parcelable

@Parcelize
data class HistoryRoom(var schedule: String?="", var roomReservation: RoomReservation): Parcelable

