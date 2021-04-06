package sv.com.credicomer.murativ2.ui.ride.models

data class Reservation(
    var reservation_available: Boolean? = false,
    var active:Boolean?=false,
    var date: String? = "",
    var id: String? = "",
    var pplsize: Int? = 0,
    var round: Int? = 0,
    var schedule: String? = "",
    var round_status: String? = "",
    var users: MutableList<String> = mutableListOf(),
    var users_tokens: MutableList<String> = mutableListOf()
)