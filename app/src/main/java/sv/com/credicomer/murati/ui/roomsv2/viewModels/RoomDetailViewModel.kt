package sv.com.credicomer.murati.ui.roomsv2.viewModels


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murati.ui.roomsv2.models.*
import sv.com.credicomer.murati.ui.roomsv2.roomRating
import sv.com.credicomer.murati.ui.alliance.models.RatingUsers
import sv.com.credicomer.murati.ui.roomsv2.calculateRating
import sv.com.credicomer.murati.ui.roomsv2.models.ListRoomItem
import sv.com.credicomer.murati.ui.roomsv2.models.RoomReservation
import sv.com.credicomer.murati.ui.roomsv2.models.RoomReservationDetail
import sv.com.credicomer.murati.ui.roomsv2.models.RoomResult
import timber.log.Timber

class RoomDetailViewModel : ViewModel() {

 /*   private var _schedule = MutableLiveData<MutableList<String>>()

    val schedule: LiveData<MutableList<String>>
        get() = _schedule

*/


    private var db= FirebaseFirestore.getInstance()

    private var auth=FirebaseAuth.getInstance()

    private var _schedule = MutableLiveData<MutableList<ListRoomItem>>()

    var _day = MutableLiveData<String>()
    val day:LiveData<String>
    get() = _day

    var _roomId = MutableLiveData<String>()
    val roomID:LiveData<String>
    get() = _roomId

    val schedule: LiveData<MutableList<ListRoomItem>>
        get() = _schedule

    private var _resevationDate = MutableLiveData<String>()
    val reservationDate: LiveData<String>
        get() = _resevationDate

    var collectionPath: String = ""

    var subCollectionPath: String= ""

    var email:String =""

    init {
        email=auth.currentUser?.email.toString()
    }

    private var _reservation = MutableLiveData<MutableMap<String,RoomReservation>>()
    val reservation: LiveData<MutableMap<String,RoomReservation>>
        get() = _reservation

    private var _reservationRT = MutableLiveData<MutableMap<String,RoomReservation>>()
    val reservationRT: LiveData<MutableMap<String,RoomReservation>>
        get() = _reservationRT

    private var _reservationList = MutableLiveData<MutableList<Reservations>>()
    val reservationList: LiveData<MutableList<Reservations>>
    get() = _reservationList

    private var _reservationListRT = MutableLiveData<MutableList<Reservations>>()
    val reservationListRT: LiveData<MutableList<Reservations>>
        get() = _reservationListRT

    private var _scheduleList = MutableLiveData<ArrayList<String>>()
    val scheduleList: LiveData<ArrayList<String>>
    get() = _scheduleList

    private var _errorSwitch = MutableLiveData<Boolean>()
    val errorSwitch: LiveData<Boolean>
    get() = _errorSwitch
/*
    private var listScheduleStatic = mutableListOf(
        "7:00AM - 7:30AM",
        "7:30AM - 8:00AM",
        "8:30AM - 9:00AM",
        "9:30AM - 10:00AM",
        "10:30AM - 11:00AM",
        "11:30AM - 12:00PM",
        "12:30PM - 1:00PM",
        "1:30PM - 2:00PM",
        "2:30PM - 3:00PM",
        "3:30PM - 4:00PM",
        "4:30PM - 5:00PM",
        "5:30PM - 6:00PM",
        "6:30PM - 7:00PM"


    )*/

    /* private var listScheduleStatic = mutableListOf(
         "07:00-07:30",
         "07:30-08:00",
         "08:00-08:30",
         "08:30-09:00",
         "09:00-09:30",
         "09:30-10:00",
         "10:00-10:30",
         "10:30-11:00",
         "11:00-11:30",
         "11:30-12:00",
         "12:00-12:30",
         "12:30-13:00",
         "13:00-13:30",
         "13:30-14:00",
         "14:00-14:30",
         "14:30-15:00",
         "15:00-15:30",
         "15:30-16:00",
         "16:00-16:30",
         "16:30-17:00",
         "17:00-17:30",
         "17:30-18:00",
         "18:00-18:30",
         "18:30-19:00"

     )*/

    private var listScheduleStatic = mutableListOf(
        ListRoomItem("07:00-07:30"),
        ListRoomItem("07:30-08:00"),
        ListRoomItem("08:00-08:30"),
        ListRoomItem("08:30-09:00"),
        ListRoomItem("09:00-09:30"),
        ListRoomItem("09:30-10:00"),
        ListRoomItem("10:00-10:30"),
        ListRoomItem("10:30-11:00"),
        ListRoomItem("11:00-11:30"),
        ListRoomItem("11:30-12:00"),
        ListRoomItem("12:00-12:30"),
        ListRoomItem("12:30-13:00"),
        ListRoomItem("13:00-13:30"),
        ListRoomItem("13:30-14:00"),
        ListRoomItem("14:00-14:30"),
        ListRoomItem("14:30-15:00"),
        ListRoomItem("15:00-15:30"),
        ListRoomItem("15:30-16:00"),
        ListRoomItem("16:00-16:30"),
        ListRoomItem("16:30-17:00"),
        ListRoomItem("17:00-17:30"),
        ListRoomItem("17:30-18:00"),
        ListRoomItem("18:00-18:30"),
        ListRoomItem("18:30-19:00")
    )

    /*private var listScheduleStatic = mutableListOf(
        "07:00-07:30",
        "07:30-08:00",
        "08:00-08:30",
        "08:30-09:00",
        "09:00-09:30",
        "09:30-10:00",
        "10:00-10:30",
        "10:30-11:00",
        "11:00-11:30",
        "11:30-12:00",
        "12:00-12:30",
        "12:30-13:00"


    )
*/

    fun getSchedule() {

        _schedule.postValue(listScheduleStatic)


    }



     fun pushRoomReservation(mutableList: MutableList<RoomResult>,roomId:String,date:String) {

        val ref = db.collection(collectionPath).document(roomId).collection(subCollectionPath)
            .document(date)

        db.runTransaction { transaction ->

            var snap = transaction.get(ref).toObject(RoomReservationDetail::class.java)

            if (snap==null){


                snap = RoomReservationDetail()



                mutableList.forEach {


                    val roomTemp= RoomReservation(email=email,rating = roomRating)
                    Timber.d("ROOMVAL %s","$roomTemp")


                    snap.roomReservations?.put(it.hour!!,roomTemp)



                }

                transaction.set(ref, snap)

            }else{
                mutableList.forEach {


                    val roomTemp= RoomReservation(email=email,rating = roomRating)
                    Timber.d("ROOMVAL %s","$roomTemp")
                    snap.roomReservations?.put(it.hour!!,roomTemp)



                }
                transaction.update(ref, "roomReservations",snap.roomReservations as Map<String, Any>)
            }

            Timber.d("SNAP %s","$snap")


            Timber.d("ROOMMAP %s","${snap.roomReservations}")




            return@runTransaction

        }.addOnSuccessListener { Timber.d("RoomRerservationSuccess %s", "Transaction success!")
        }

            .addOnFailureListener { e -> Timber.w("RoomRerservationFailure %s", "Transaction failure.$e") }
    }


    fun getRoomReservation(roomId:String, date:String){
        var roomRef = db.collection(collectionPath).document(roomId)
            roomRef.addSnapshotListener { snapshot, e ->
                    val rooms = snapshot!!.toObject(Room::class.java)
                        snapshot.reference.collection(subCollectionPath).document(date).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                            val roomReservations = documentSnapshot!!.toObject(RoomReservationDetail::class.java)

                            if (roomReservations== null){
                                _reservation.value = null
                                _resevationDate.value = null
                            }
                            else{
                                _reservation.value = roomReservations.roomReservations
                                _resevationDate.value = roomReservations.roomDetailId
                            }

                        }
                }


    }

    fun updateRating(rate: Int,roomId: String,scheule:String,roomDetailId: String,collectionPath:String,subCollectionPath:String) {
        val ref = db.collection(collectionPath).document(roomId)
            .collection(subCollectionPath)
            .document(roomDetailId)

        Timber.d("UPDATE_RANTING %s", "$roomId,$roomDetailId, $collectionPath, $subCollectionPath")

        db.runTransaction { transaction ->

            val snap = transaction.get(ref).toObject(RoomReservationDetail::class.java)

                snap?.roomReservations!![scheule]?.ratedUsers?.add(RatingUsers(email = email, rating = rate))

                Timber.d("schedule %s", "${snap.roomReservations!![scheule]}")

            Timber.d("RESER %s", "${snap.roomReservations!![scheule]?.ratedUsers}")
            when (rate) {

                1 -> {
                    snap.roomReservations!![scheule]?.rating!!["one"] = snap.roomReservations!![scheule]?.rating!!["one"]!!.plus(1)
                }
                2 -> {
                    snap.roomReservations!![scheule]?.rating!!["two"] = snap.roomReservations!![scheule]?.rating!!["two"]!!.plus(1)
                }
                3 -> {
                    snap.roomReservations!![scheule]?.rating!!["three"] = snap.roomReservations!![scheule]?.rating!!["three"]!!.plus(1)
                }
                4 -> {
                    snap.roomReservations!![scheule]?.rating!!["four"] = snap.roomReservations!![scheule]?.rating!!["four"]!!.plus(1)
                }
                5 -> {
                    snap.roomReservations!![scheule]?.rating!!["five"] = snap.roomReservations!![scheule]?.rating!!["five"]!!.plus(1)
                }
            }
            Timber.d("RESER2 %s", "${snap.roomReservations!![scheule]?.rating}")

            snap.roomReservations!![scheule]?.rating?.let { snap.roomReservations!![scheule]?.ratingAvg= calculateRating(it) }

            Timber.d("the rating avg is %s ","${snap.roomReservations!![scheule]?.ratingAvg}")

            transaction.update(ref, "roomReservations", snap.roomReservations)

            return@runTransaction



        }.addOnSuccessListener {
            Timber.d("FStransact Success %s", "Transaction success!")

        }
            .addOnFailureListener { e -> Timber.w("FStransact Failure %s", "Transaction failure. $e") }
    }


    fun deleteReservation(schedule: String, roomId: String, date: String){
        val ref = db.collection(collectionPath).document(roomId).collection(subCollectionPath).document(date)
        val field = hashMapOf<String, Any>(
            "roomReservations.${schedule}" to FieldValue.delete()
        )
        ref.update(field).addOnSuccessListener {
            Timber.d("TAG %s", "Field was success deleted, schedule: $schedule")
        }.addOnFailureListener {
            Timber.d("TAG %s", "there was an error, error: $it")
        }
    }


    fun getDates(today: String){
        val roomDetail = mutableListOf<Reservations>()
        db.collection(collectionPath).get().addOnSuccessListener { allRooms ->
            allRooms.documents.forEach {rooms ->
                val roomInfo = rooms.toObject(Room::class.java)
                rooms.reference.collection(subCollectionPath)
                    .get().addOnSuccessListener {dates ->
                        dates.documents.forEach {date ->
                            val reservationDetail = date.toObject(RoomReservationDetail::class.java)
                            val reservation = reservationDetail!!.roomReservations
                            if(reservationDetail.roomDetailId.toString() == today){
                                reservation!!.keys.forEach{
                                    if (email == reservation[it]!!.email)
                                    {
                                    val room = Reservations("${roomInfo!!.roomName}", "${reservationDetail.roomDetailId}",
                                        it, "${roomInfo.roomId}")
                                    roomDetail.add(room)
                                    }
                                }
                            }

                        }
                        _reservationList.value = roomDetail
                    }

            }

        }

    }

    fun getSchedules(list: MutableList<RoomResult>,roomId: String, date: String){
        db.collection(collectionPath).document(roomId).collection(subCollectionPath).document(date)
            .get().addOnSuccessListener {
                if (it.exists()) {
                    val reservationDetail = it.toObject(RoomReservationDetail::class.java)
                    if (reservationDetail!!.roomReservations!!.isEmpty()) {
                        _errorSwitch.value = false
                    } else {
                        val reservation = reservationDetail!!.roomReservations
                        loop@ for (schedules in reservation!!.keys){

                           for (time in listScheduleStatic)  {

                               val scheduleSplitted = schedules.split("-")
                               val timeSplitted = time.schedule!!.split("-")
                                if (timeSplitted[0] >= scheduleSplitted[0] && timeSplitted[1] <= scheduleSplitted[1]) {
                                   for (objects in list)  {
                                        var items = objects.interval
                                        if (timeSplitted[0] >= items!![0] && timeSplitted[1] <= items[1]){
                                            _errorSwitch.value = true
                                            break@loop
                                        }
                                       else{
                                            _errorSwitch.value = false
                                        }
                                    }
                                }

                            }
                        }
                    }


                }
                else{
                    _errorSwitch.value = false
                }
            }
    }

    fun funPrueba(context: Context){
        Toast.makeText(context ,"day: ${_day.value}, roomId: ${_roomId.value}", Toast.LENGTH_SHORT).show()
    }


}