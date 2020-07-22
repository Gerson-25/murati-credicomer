package sv.com.credicomer.murati.ui.ride.models

data class ReservationFS(var id:String?="",
                         var pplsize:Int?=0,
                         var reservation_available:Boolean?=true,
                         var round_status:String?="",
                         var schedule_time:String?="",
                         var time_interval:String?="",
                         var users:MutableList<String>?= mutableListOf(),
                         var index:Int?=0,
                         var tokens:MutableList<String>?= mutableListOf()
                         )

