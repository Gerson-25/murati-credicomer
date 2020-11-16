package sv.com.credicomer.murativ2.ui.roomsv2.viewModels


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.ui.roomsv2.models.*
import sv.com.credicomer.murativ2.ui.roomsv2.roomRating
import sv.com.credicomer.murativ2.ui.alliance.models.RatingUsers
import sv.com.credicomer.murativ2.ui.roomsv2.calculateRating
import sv.com.credicomer.murativ2.ui.roomsv2.models.ListRoomItem
import sv.com.credicomer.murativ2.ui.roomsv2.models.RoomReservation
import sv.com.credicomer.murativ2.ui.roomsv2.models.RoomReservationDetail
import sv.com.credicomer.murativ2.ui.roomsv2.models.RoomResult
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

    var _updating = MutableLiveData<Boolean>()
    val updating:LiveData<Boolean>
        get() = _updating

    var _startTime = MutableLiveData<String>()
    val startTime:LiveData<String>
    get() = _startTime

    var _endTime = MutableLiveData<String>()
    val endTime:LiveData<String>
        get() = _endTime


    val schedule: LiveData<MutableList<ListRoomItem>>
        get() = _schedule

    private var _selectedDay = MutableLiveData<Int>()
    val selectedDay:LiveData<Int>
    get() = _selectedDay

    private var _resevationDate = MutableLiveData<String>()
    val reservationDate: LiveData<String>
        get() = _resevationDate

    var collectionPath: String = ""

    var subCollectionPath: String= ""

    var email:String =""

    private var _reservationCounter = MutableLiveData<Int>()
    val reservationCounter: LiveData<Int>
        get() = _reservationCounter

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

    private var _reservationListRT = MutableLiveData<MutableList<ReservationList>>()
    val reservationListRT: LiveData<MutableList<ReservationList>>
        get() = _reservationListRT

    private var _scheduleList = MutableLiveData<ArrayList<String>>()
    val scheduleList: LiveData<ArrayList<String>>
    get() = _scheduleList

    private var _emails = MutableLiveData<List<String>>()
    val emails:LiveData<List<String>>
    get() = _emails

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _errorSwitch = MutableLiveData<Boolean>()
    val errorSwitch: LiveData<Boolean>
    get() = _errorSwitch

    private var _lisOfSchedule = MutableLiveData<MutableList<ScheduleList>>()
    val listOfSchedule: LiveData<MutableList<ScheduleList>>
     get() = _lisOfSchedule

    private var _lisOfScheduleEnd = MutableLiveData<MutableList<ScheduleList>>()
    val listOfScheduleEnd: LiveData<MutableList<ScheduleList>>
        get() = _lisOfScheduleEnd

    private var _lisOfEmails = MutableLiveData<MutableList<String>>()
    val listOfEmails: LiveData<MutableList<String>>
        get() = _lisOfEmails
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

    fun getAllSchedules() = mutableListOf(
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
    )

    fun setStartTime(time: String){
        _startTime.value = time
    }

    fun getEmail(emails:MutableList<String>){
        _lisOfEmails.value = emails
    }

    fun setEndTime(time:String){
        _endTime.value = time
    }

    fun getSchedule() {
        _schedule.postValue(listScheduleStatic)
    }

    fun getAvailableSchedules(roomId: String, date: String){
        db.collection("rooms").document(roomId).collection("reservations").whereEqualTo("date",date)
            .get().addOnSuccessListener {
                val items = getAllSchedules()
                val splittedItems = getAllSchedules().map {
                    it.split("-")
                }
                val reservations = it.toObjects(ReservationList::class.java)
                if (reservations.isEmpty()){
                    _lisOfSchedule.value = items.map {
                        ScheduleList(it, true)
                    }.toMutableList()
                }
                else{
                    val reservedScehdules = reservations.map {
                        listOf(it.start, it.end)
                    }
                    var availableSchedules = mutableListOf<ScheduleList>()
                    splittedItems.forEachIndexed { index, list ->
                        reservedScehdules.forEach {
                            if (list[0] >= it[0] && list[1] <= it[1]){
                                availableSchedules.add(ScheduleList(items[index], false))
                            }
                            else{
                                availableSchedules.add(ScheduleList(items[index], true))
                            }
                        }
                    }
                    var unavailableSchedules = availableSchedules.filter {
                        !it.available
                    }

                    var unavailableItems = unavailableSchedules.map {
                        it.schedule
                    }

                    var newListOfSchedules = mutableListOf<ScheduleList>()

                    items.forEach { item ->
                        var detector = 0
                        unavailableItems.forEach {unavailableItem ->
                            if (item == unavailableItem){
                                detector = 1
                            }
                        }
                        if (detector ==1){
                            newListOfSchedules.add(ScheduleList(item, false))
                        }
                        else{
                            newListOfSchedules.add(ScheduleList(item, true))
                        }
                    }



                    _lisOfSchedule.value = newListOfSchedules
                }

            }.addOnFailureListener {
            }
    }

    fun getEndAvailableSchedule(roomId: String, date: String, startTime: String){

        db.collection("rooms").document(roomId).collection("reservations").whereEqualTo("date",date)
            .get().addOnSuccessListener {

                val items = getAllSchedules()
                val splittedItems = getAllSchedules().map {
                    it.split("-")
                }

                val reservations = it.toObjects(ReservationList::class.java)
                if (reservations.isEmpty()){
                    var list = items.map {
                        ScheduleList(it, true)
                    }.toMutableList()

                    list.retainAll {
                        it.schedule.split("-")[0] >= startTime
                    }
                    _lisOfScheduleEnd.value = list
                }
                else{
                    val reservedScehdules = reservations.map {
                        listOf(it.start, it.end)
                    }
                    var availableSchedules = mutableListOf<ScheduleList>()

                    var count = 0
                    reservedScehdules.forEach{ reservation ->
                        splittedItems.forEachIndexed { index, list ->
                            if (list[0] <= startTime){
                            }
                            else if (list[0] <= reservation[0] && count == 0){
                                availableSchedules.add(ScheduleList(items[index], true))
                                if ((list[0] == reservation[0])){
                                    count = 1
                                }
                            }
                            else if (list[0] >= startTime && count == 0){
                                availableSchedules.add(ScheduleList(items[index], true))
                            }
                        }
                    }
                    _lisOfScheduleEnd.value = availableSchedules
                    Log.d("TAG", "list of available schedules: $availableSchedules")
                }

            }
    }

    @SuppressLint("LogNotTimber")
    fun increaseReservationCounter(operation:String){
        when(operation){
            "sumar" ->{
                if (_reservationCounter.value == null){
                    _reservationCounter.value = 1
                }
                else{
                    _reservationCounter.value = _reservationCounter.value?.plus(1)
                }
            }
                "restar"-> {
                    if (_reservationCounter.value == 0){
                        _reservationCounter.value = 0
                    }
                    else{
                        _reservationCounter.value = _reservationCounter.value?.minus(1)
                    }
                }
            else ->{
                _reservationCounter.value = 0
            }
        }

    }

    fun getEmails(){
        db.collection("users").get().addOnSuccessListener {
            val users = it.toObjects(Users::class.java)
            val emails = users.map {
                it.email
            }
            _emails.value = emails
            Log.d("TAG", "list in viewmodel: ${it.documents}")
        }
    }

    fun saveNewReservation(room: Room, reservation: HashMap<String,Any>){
        _loading.value = true
        reservation.put("organizer", email)
        db.collection("rooms").document(room.roomId!!).collection("reservations").document().set(reservation).addOnSuccessListener {
            _loading.value = false
        }.addOnFailureListener {
        }
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
            _updating.value = false
        }

            .addOnFailureListener { e -> Timber.w("RoomRerservationFailure %s", "Transaction failure.$e") }
    }


    fun getRoomReservation(roomId:String, date:String){
        db.collection("rooms")
            .document(roomId).collection("reservations")
            .whereEqualTo("date", date).get().addOnSuccessListener {
                val reservations = it.toObjects(ReservationList::class.java)
                _reservationListRT.value = reservations
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


    fun deleteReservation(id:String, roomname:String){
        val ref = db.collection("rooms").document(roomname).collection("reservations").document(id)
        ref.delete().addOnSuccessListener {

        }.addOnFailureListener {
        }
    }


    fun getMyReservations(today: String){
        db.collection("rooms").document().collection("reservations")
    }


    @SuppressLint("LogNotTimber")
    fun getDates(today: String){
        var reservationList = mutableListOf<ReservationList>()
        db.collection("rooms").get().addOnSuccessListener { snapshot ->
            snapshot.documents.forEach {
                it.reference.collection("reservations").whereEqualTo("organizer", email).whereEqualTo("date", today).get().addOnSuccessListener {
                    val reservations = it.toObjects(ReservationList::class.java)
                    reservations.forEach {reservation->
                        reservationList.add(reservation)
                        _reservationListRT.value = reservationList
                    }

                }

            }

        }
        _reservationListRT.value = reservationList
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

    fun setDayAndId(date: String, roomId: String) {
        _day.value = date
        _roomId.value = roomId
    }

    fun selectDay(position:Int, date: String, dateFormatted:String){
        _day.value = dateFormatted
        _selectedDay.value = position
    }


}