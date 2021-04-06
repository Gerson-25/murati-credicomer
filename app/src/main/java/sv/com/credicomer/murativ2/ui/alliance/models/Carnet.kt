package sv.com.credicomer.murativ2.ui.alliance.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserCarnet(
    var email: String? = "",
    var name: String? = "",
    var collaboratorCod: String? = "",
    var departmentName: String? = "",
    var carnetPhoto: String? = "",
    var token:String? =""
) : Parcelable {
    constructor(name: String?, collaboratorCod: String?, departmentName: String?) : this() {
        this.name = name
        this.collaboratorCod = collaboratorCod
        this.departmentName = departmentName
    }

    constructor(
        name: String?,
        collaboratorCod: String?,
        departmentName: String?,
        carnetPhoto: String?
    ) : this() {
        this.name = name
        this.collaboratorCod = collaboratorCod
        this.departmentName = departmentName
        this.carnetPhoto = carnetPhoto

    }
}