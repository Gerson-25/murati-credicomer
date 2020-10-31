package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Room(var roomId:String?="",
                var equipment:MutableList<String>?= mutableListOf(),
                var capacity:String?="",
                var location:String?="",
                var roomName:String?="",
                var roomImages:MutableList<String>?= mutableListOf()
                ):Parcelable