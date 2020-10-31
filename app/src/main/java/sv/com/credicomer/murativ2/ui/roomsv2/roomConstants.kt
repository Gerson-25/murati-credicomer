package sv.com.credicomer.murativ2.ui.roomsv2

val roomStaticEquipent= mutableListOf("pantalla","televisor","telefono","proyector", "webcam", "pizarra")

val roomStaticFloorLevels= mutableListOf("Nivel 1","Nivel 2","Nivel 3","Nivel 4","Nivel 5")

val roomStaticCapacity = Array(19) { "${it+2}" }

val roomRating= mutableMapOf("five" to 0,
                "four" to 0,
                "three" to 0,
                "two" to 0,
                "one" to 0

)


  var listScheduleStatic = mutableListOf(
         "07:00",
         "07:30",
         "08:00",
         "08:30",
         "09:00",
         "09:30",
         "10:00",
         "10:30",
         "11:00",
         "11:30",
         "12:00",
         "12:30",
         "13:00",
         "13:30",
         "14:00",
         "14:30",
         "15:00",
         "15:30",
         "16:00",
         "16:30",
         "17:00",
         "17:30",
         "18:00",
         "18:30"

     )

const val NOW="Hoy"
const val TOMORROW = "Mañana"


const val CAPACITY_FILTER="capacity"
const val LOCATION_FILTER="location"
const val EQUIPMENT_FILTER="equipment"
const val HOUR_FILTER="hour"
const val DATE_FILTER ="date"
const val NAME_FILTER="roomName"

