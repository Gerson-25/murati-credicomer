package sv.com.credicomer.murativ2.ui.ride.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.ride.getDateJoda
import sv.com.credicomer.murativ2.ui.ride.models.ReservationFS
import sv.com.credicomer.murativ2.ui.ride.models.User
import sv.com.credicomer.murativ2.ui.ride.models.UserState
import sv.com.credicomer.murativ2.ui.ride.subscribeTopicNotifications
import sv.com.credicomer.murativ2.ui.ride.unsubscribeTopicNotifications
import timber.log.Timber

class ReservationFSViewModel : ViewModel() {

    private var auth = FirebaseAuth.getInstance().currentUser

    private var firestoreDb = FirebaseFirestore.getInstance()

    var user: User

    private var todayDate: String

    private var _reservations = MutableLiveData<MutableList<ReservationFS>>()

    private var _userStates = MutableLiveData<UserState>()

    private var _userCheckCallback = MutableLiveData<Boolean>()

    private var _token=MutableLiveData<String>()

    val token:LiveData<String>
        get()=_token

    val reservations: LiveData<MutableList<ReservationFS>>
        get() = _reservations

    val userState: LiveData<UserState>
        get() = _userStates


    init {

        _reservations.value = mutableListOf()
        user = User(auth?.email, auth?.uid)
        todayDate = getDateJoda()

    }


    fun getAllReservations(): LiveData<MutableList<ReservationFS>> {


        val ref = firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .orderBy("index", Query.Direction.ASCENDING)
        ref.addSnapshotListener { snap, e ->

            if (e != null) {
                Timber.w("GET-RESERVATIONS %s", "Listen failed.")
                return@addSnapshotListener
            }

            if (snap != null && snap.documents.isNotEmpty()) {

                Timber.d("rsv-data %s", "Current data: ${snap.documents}")

                _reservations.value = snap.toObjects(ReservationFS::class.java)
            } else {
                Timber.d("rsv-data %s", "Current data: null")
            }

        }

        return _reservations
    }

    fun getUSerStates(): MutableLiveData<UserState> {

        firestoreDb.collection("users").document(user.email.toString()).collection("reservations")
            .document(todayDate).get()
            .addOnSuccessListener {

                _userStates.value = it.toObject(UserState::class.java)


            }


        return _userStates
    }


    fun checkUserReservation(
        reservationFS: ReservationFS,
        context: Context
    ): MutableLiveData<Boolean> {

        val query = firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .whereEqualTo("users", user.email)
            .whereEqualTo("schedule_time", reservationFS.schedule_time).get()

        query.addOnCompleteListener {

            if (!it.isSuccessful && it.result!!.isEmpty) {

                Timber.d("checkuser %s", "the user is already in ${reservationFS.schedule_time} ")
                Toast.makeText(context, "The user is already in", Toast.LENGTH_LONG).show()
                //deleteReservation()
                _userCheckCallback.postValue(false)

            }


            Timber.d("checkuser %s", "the user is not in ${reservationFS.schedule_time} ")
            Toast.makeText(context, "The user is not in", Toast.LENGTH_LONG).show()
            _userCheckCallback.postValue(true)


        }


        return _userCheckCallback


    }


    private fun pushReservation(reservationFS: ReservationFS,context: Context) {

        Log.d("TAG", "round id: ${reservationFS.id.toString()}")


        val ref = firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .document(reservationFS.id.toString())
        var transactSuccess=true
        firestoreDb.runTransaction { transaction ->

            val snap = transaction.get(ref).toObject(ReservationFS::class.java)

            if(snap?.pplsize!! > 0){
            user.email?.let { snap.users?.add(it) }

            snap.pplsize = snap.pplsize?.minus(1)

            Timber.d("GETTOKEN %s", "${_token.value}")

            snap.tokens?.add(_token.value!!)
                transaction.update(ref, "pplsize", snap.pplsize)
                transaction.update(ref, "users", snap.users)
                transaction.update(ref,"tokens", snap.tokens)
                return@runTransaction
            }else{
               transactSuccess=false
                return@runTransaction
            }


        }.addOnSuccessListener { Timber.d("FStransact Success %s", "Transaction success!")
            if (transactSuccess){
                val index = reservationFS.index.toString()
                val topic = "round$index"
                subscribeTopicNotifications(topic)
            }else{
                Toast.makeText(context,"No hay reservaciones disponibles para el horario ",Toast.LENGTH_LONG).show()
            }

        }
            .addOnFailureListener { e -> Timber.w("FStransact Success %s", "Transaction failure.")

            }

    }


    private fun deleteUsers(reservationFS: ReservationFS,context: Context) {

        var transactSuccess=true
        val ref = firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .document(reservationFS.id.toString())

        firestoreDb.runTransaction { transaction ->

            val snap = transaction.get(ref).toObject(ReservationFS::class.java)

            if (snap?.pplsize!! > 0) {
            user.email?.let { snap.users?.remove(it) }

            snap.pplsize = snap.pplsize?.plus(1)

            snap.tokens?.remove(_token.value)


                transaction.update(ref, "pplsize", snap.pplsize)
                transaction.update(ref, "users", snap.users)
                transaction.update(ref, "tokens", snap.tokens)
                return@runTransaction
            }else{
                transactSuccess=false
                return@runTransaction
            }




        }.addOnSuccessListener { Timber.d("FStransact Success %s", "Transaction success!")

            if (transactSuccess){
            val index = reservationFS.index.toString()
            val topic = "round$index"
            unsubscribeTopicNotifications(topic)
            }else{
                Toast.makeText(context,"No hay reservaciones disponibles para el horario ",Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener { e -> Timber.w("FStransact Success %s", "Transaction failure.")

            }

    }

    private fun deleteUsers(reservationFS: ReservationFS, previousReservation: ReservationFS,context: Context) {

        val targetRef =firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .document(reservationFS.id.toString())
        val ref = firestoreDb.collection("reservations").document(todayDate).collection("rounds")
            .document(previousReservation.id.toString())
        var pushReservation=true
        firestoreDb.runTransaction { transaction ->

            val targetSnap=transaction.get(targetRef).toObject(ReservationFS::class.java)
            val snap = transaction.get(ref).toObject(ReservationFS::class.java)

            if (targetSnap?.pplsize!! >0) {
                user.email?.let { snap?.users?.remove(it) }

                snap?.pplsize = snap?.pplsize?.plus(1)

                snap?.tokens?.remove(_token.value)
                transaction.update(ref, "pplsize", snap?.pplsize)
                transaction.update(ref, "users", snap?.users)
                transaction.update(ref, "tokens", snap?.tokens)
                return@runTransaction

            }else{
                pushReservation=false
                return@runTransaction

            }


        }.addOnSuccessListener {
            Timber.d("FStransact Success %s", "Transaction success!")
                if (pushReservation){ pushReservation(reservationFS,context)
                    val index = reservationFS.index.toString()
                    val topic = "round$index"
                    val previousIndex = previousReservation.index.toString()
                    val previousTopic = "round$previousIndex"

                    unsubscribeTopicNotifications(previousTopic)
                    subscribeTopicNotifications(topic)

                }else{
                    Toast.makeText(context,"No hay reservaciones disponibles para el horario ",Toast.LENGTH_LONG).show()
                }

        }
            .addOnFailureListener { e -> Timber.w("FStransact Success %s", "Transaction failure.")

            }

    }

    fun deleteReservation(reservationFS: ReservationFS, context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_cancel_title))
            .setMessage(context.getString(R.string.dialog_cancel_description))
            .setPositiveButton(context.getString(R.string.dialog_cancel_positive_btn)) { _, _ ->

                deleteUsers(reservationFS,context)


            }
            .setNegativeButton(context.getString(R.string.dialog_confirmation_negative_btn)) { _, _ ->

            }
        alertDialog.show()
    }

    fun deleteReservation(
        reservationFS: ReservationFS,
        context: Context,
        previousReservation: ReservationFS
    ) {

        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_cancel_title))
            .setMessage(
                context.getString(
                    R.string.dialog_update_description,
                    previousReservation.time_interval
                )
            )
            .setPositiveButton(context.getString(R.string.dialog_update_positive_btn)) { _, _ ->

                deleteUsers(reservationFS, previousReservation,context)

            }
            .setNegativeButton(context.getString(R.string.dialog_confirmation_negative_btn)) { _, _ ->

            }
        alertDialog.show()

    }

    fun confirmReservation(reservationFS: ReservationFS, context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.dialog_confirmation_title))
            .setMessage(
                context.getString(
                    R.string.dialog_confirmation_desciption,
                    reservationFS.time_interval
                )
            )
            .setPositiveButton(context.getString(R.string.dialog_confirmation_positive_btn)) { _, _ ->


                pushReservation(reservationFS,context)


            }
            .setNegativeButton(context.getString(R.string.dialog_confirmation_negative_btn)) { _, _ ->

            }
        alertDialog.show()
    }

    fun getToken() {

        var token: String?
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Timber.w("TOKEN %s", "getInstanceId failed  ${task.exception}")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                token = task.result?.token

                _token.postValue(token)

                // Log and toast
                val msg = "the generated token -> $token"
                Timber.d("TOKEN %s", msg)


            })
        // [END retrieve_current_token]

    }




}