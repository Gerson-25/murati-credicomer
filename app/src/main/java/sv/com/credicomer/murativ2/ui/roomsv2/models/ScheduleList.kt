package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ScheduleList(var schedule: String = "", var available: Boolean = true):Parcelable