package sv.com.credicomer.murativ2.ui.roomsv2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(var carnetPhoto: String = "",
                 var collaboratorCod:String = "",
                 var departmentName: String = "",
                 var email:String = "",
                 var name:String = "",
                 var token: String = ""):Parcelable