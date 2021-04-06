package sv.com.credicomer.murativ2.ui.request.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdditionalInfoModel(
    val addres: String? = null,
    val presonalReferences: HashMap<String, References>
):Parcelable

@Parcelize
data class References(
    val name: String? = null,
    val phoneNumber: String? = null
): Parcelable

class GeneralInfo {
    var nombre:String? = null
    var direccion:String? = null
    var ocupacion:String? = null
    var telefono:String? = null
}
