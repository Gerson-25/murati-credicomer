package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import sv.com.credicomer.murativ2.ui.alliance.models.RatingUsers
@Parcelize
data class RoomReservationDetail(
    var roomId: String? = "",
    var roomDetailId: String? = "",
    var roomReservations: MutableMap<String, RoomReservation>? = mutableMapOf()
): Parcelable
@Parcelize
data class RoomReservation(
    var email: String?="",
    var rating: MutableMap<String, Int>? = hashMapOf(),
    var ratedUsers: MutableList<RatingUsers>?= mutableListOf(),
    var ratingAvg:Double?=0.0
) : Parcelable


