package sv.com.credicomer.murati.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomQuery (var field:String,var value:MutableList<String>) : Parcelable

@Parcelize
data class RoomQueryHistory (var field:String,var value:MutableList<Room>) : Parcelable