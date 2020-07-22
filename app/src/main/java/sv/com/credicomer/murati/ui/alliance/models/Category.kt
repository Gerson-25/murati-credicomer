package sv.com.credicomer.murati.ui.alliance.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    var category: String? = "",
    var establishments: Establishment? = Establishment()
) : Parcelable


@Parcelize
data class Establishment(
    var description: String? = "",
    var direction: String? = "",
    var phone_number: String? = "",
   // var promotions:MutableList<Promotion>?= mutableListOf(),
    var promotions:Promotion?= Promotion(),
    var establishment: String? = ""


) : Parcelable

@Parcelize
@IgnoreExtraProperties
data class Promotion(
    var date: String? = "",
    var description: String? = "",
    var id: String? = "",
    var image: String? = "",
    var likes: Int? = 0,
    var promotion_name: String? = "",
    var rating: Double? = 0.0


) : Parcelable