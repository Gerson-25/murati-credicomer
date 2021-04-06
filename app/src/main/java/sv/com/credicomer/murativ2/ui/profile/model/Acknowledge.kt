package sv.com.credicomer.murativ2.ui.profile.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class
Acknowledge(val type:String = "", val message:String = "", val postTime:String = "", val id:String =""): Parcelable