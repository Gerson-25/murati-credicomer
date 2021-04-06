package sv.com.credicomer.murativ2.ui.alliance.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryFS(
    var category_id: String? = ""
) : Parcelable


@Parcelize
data class EstablishmentFS(
    var description: String? = "",
    var direction: String? = "",
    var phone_number: String? = "",
    var establishment: String? = "",
    var establishment_id:String="",
    var category_id: String?="",
    var establishment_image: String?=""


) : Parcelable

@Parcelize
data class PromotionFS(
    //var date:String?="",
    var date: Timestamp?= Timestamp.now(),
    var description: String? = "",
    var promotion_id: String? = "",
    var image: String? = "",
    var category_id: String? ="",
    var promotion_name: String? = "",
    var rating: HashMap<String,Int>?= hashMapOf(),
    var rating_avg: Double?=0.0,
    var establishment_id: String?="",
    var estado: Boolean? = true,
    var establishment:String?="",
    var rated_users:MutableList<RatingUsers>?= mutableListOf(),
    var description_establishment: String? ="",
    var direction_establishment: String?="",
    var phone_number: String?="",
    var establishment_image: String?=""
) : Parcelable
@Parcelize
data class RatingUsers(
    var email:String?="",
    var rating:Int?=0,
    var comment:String? ="",
    var receipe_number: String? = ""
): Parcelable

