package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ReservationList(var room: String = "",
                      var date: String = "",
                      var start: String = "",
                      var end: String = "",
                      var title: String = "",
                      var organizer: String = "",
                      var id:String = "",
                      var attendees: MutableList<String> = mutableListOf()
):Parcelable