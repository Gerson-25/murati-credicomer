package sv.com.credicomer.murativ2.ui.travel.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Travel(
    val originCountry: String?="",
    val destinyCountry: String?="",
    val cash: String?="",
    val emailUser: String?="",
    val initialDate: String?="",
    val finishDate: String?="",
    val dateRegister: String?="",
    val updateRegister: String?="",
    val balance: String?="",
    val active: Boolean=false,
    val settled: Boolean=false,
    val travelId:String?=""
) : Parcelable 