package sv.com.credicomer.murati.ui.ride.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murati.ui.ride.getDateJoda
import timber.log.Timber

class MapViewModel : ViewModel() {

    private var firestoredb = FirebaseFirestore.getInstance()
    private var _mapStatus = MutableLiveData<Boolean>()
    private var todayDate: String = getDateJoda()

    val mapStatus:LiveData<Boolean>
        get() = _mapStatus


    fun getMapStat(): LiveData<Boolean> {


        val ref = firestoredb.collection("reservations").document(todayDate)

        ref.addSnapshotListener { snap, e ->

            if (e != null) {
                Timber.w("GET-RESERVATIONS", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snap != null ) {

                Timber.d("rsv-data", "Current data: ${snap["is_service_activated"]}")
                if(snap["is_service_activated"]!=null)
                    _mapStatus.value = snap["is_service_activated"] as Boolean
            } else {
                Timber.d("rsv-data", "Current data: null")
            }

        }

        return _mapStatus
    }

}