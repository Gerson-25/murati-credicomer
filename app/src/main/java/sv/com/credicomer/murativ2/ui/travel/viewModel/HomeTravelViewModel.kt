package sv.com.credicomer.murativ2.ui.travel.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.ui.travel.models.Record
import sv.com.credicomer.murativ2.ui.travel.models.Travel
import timber.log.Timber

class HomeTravelViewModel : ViewModel() {

    //Firebase reference
    val db = FirebaseFirestore.getInstance()
    var travelRef = db.collection("e-Tracker")


    private var _records = MutableLiveData<MutableList<Record>>()
    val records: LiveData<MutableList<Record>>
        get() = _records

    private var _recordCounter = MutableLiveData<HashMap<String,Double>>()
    val recordCounter: LiveData<HashMap<String,Double>>
        get() = _recordCounter


    private val _viaje = MutableLiveData<MutableList<Travel>>()
    val viaje: LiveData<MutableList<Travel>>
        get() = _viaje


    //getting data for header
    fun getDataHeader(id: String) {

        Timber.d("IDHEADER %s", id)
       // val query = travelRef.whereEqualTo("travelId", id)
        db.collection("e-Tracker").whereEqualTo("travelId",id).get().addOnSuccessListener {
            val travel= it.toObjects(Travel::class.java)
            Timber.d("GETDATAHEADER %s","$travel")
            _viaje.postValue(travel)
        }
    }

    //get data for category and recyclerView
    fun getRecordsValues(id:String){
        val recordCounters = hashMapOf(
            "totalFood" to 0.0,
            "totalTrasport" to 0.0,
            "totalHotel" to 0.0,
            "others" to 0.0
        )
        db.collection("e-Tracker").document(id).collection("record")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //val tempRecords = querySnapshot!!.toObjects(Record::class.java)
                var tempRecords = querySnapshot!!.toObjects(Record::class.java)
                //val idRecord = querySnapShot.documents[0].id
                //_records.value= tempRecords
                Timber.d("RECORDS %s", "The records are ->${_records.value} ")

                tempRecords.forEach { record ->
                    when (record.recordCategory) { //verifco la categoria a la que pertecene cada gasto
                        "0" -> //si es comida acumula su cantidad en una variable
                            recordCounters["totalFood"] =
                                recordCounters["totalFood"]!!.plus(record.recordMount!!.toDouble())
                        "1" -> // transporte
                            recordCounters["totalTrasport"] =
                                recordCounters["totalTrasport"]!!.plus(record.recordMount!!.toDouble())
                        "2" -> //hospedaje
                            recordCounters["totalHotel"] =
                                recordCounters["totalHotel"]!!.plus(record.recordMount!!.toDouble())
                        "3" -> //Otros
                            recordCounters["others"] =
                                recordCounters["others"]!!.plus(record.recordMount!!.toDouble())
                    }
                    Timber.d("RECORDSCOUNTERS %s", "The records are ->${_recordCounter.value} ")
                }
                _recordCounter.value = recordCounters
        }
    }
    fun getRecords(id:String){
        db.collection("e-Tracker").document(id).collection("record")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                //val tempRecords = querySnapshot!!.toObjects(Record::class.java)
                var tempRecords = querySnapshot!!.toObjects(Record::class.java)
                //val idRecord = querySnapShot.documents[0].id
                _records.value= tempRecords
                Timber.d("RECORDS %s", "The records are ->${_records.value} ")
            }
    }

}