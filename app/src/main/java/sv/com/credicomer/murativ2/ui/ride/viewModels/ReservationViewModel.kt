package sv.com.credicomer.murativ2.ui.ride.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import sv.com.credicomer.murativ2.ui.ride.getDateJoda
import sv.com.credicomer.murativ2.ui.ride.models.LatLang
import sv.com.credicomer.murativ2.ui.ride.models.Reservation
import sv.com.credicomer.murativ2.ui.ride.models.User
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
class ReservationViewModel : ViewModel() {

    private var auth=FirebaseAuth.getInstance().currentUser

    var user:User

    private var _reservations = MutableLiveData<MutableList<Reservation>>()

    private var _activeReservations = MutableLiveData<MutableList<Reservation>>()

    private var _listOfHours = MutableLiveData<MutableList<String>>()

    private var _location=MutableLiveData<LatLng>()

    private var firestoredb = FirebaseDatabase.getInstance().getReference("reservations")

    private var todayDate: String = getDateJoda()


    val reservations: LiveData<MutableList<Reservation>>
        get() = _reservations


    val location:LiveData<LatLng>
        get() = _location


    init {
        _reservations.value = mutableListOf()
        _activeReservations.value = mutableListOf()
        _listOfHours.value= mutableListOf("3:00", "3:15","3:30","3:45","4:00")
        user=User(auth?.email,auth?.uid)

    }

     fun getLocation(){


        firestoredb.child(todayDate).child("location").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){

                    val coordinate = dataSnapshot.getValue(LatLang::class.java)


                    val coor= LatLng(
                        coordinate?.latitude!!,
                        coordinate.longitude!!
                    )

                    _location.value=coor

                    Timber.d("LATLANG-coordinate","$coordinate")



                }else{

                    Timber.d("LATLANG","NO EXISTE")

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

    }





}