package sv.com.credicomer.murativ2.ui.travel.viewModel

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import sv.com.credicomer.murativ2.R
import sv.com.credicomer.murativ2.ui.travel.models.Travel

class TravelRegistrationViewModel : ViewModel() {

    var _isActive = MutableLiveData<Boolean>()
    val isActive:LiveData<Boolean>
    get() = _isActive


    private var _isDataSent = MutableLiveData<String>()
    val isDataSent: LiveData<String>
        get() = _isDataSent
    private var _viaje = MutableLiveData<MutableList<Travel>>()
    val viaje: LiveData<MutableList<Travel>>
        get()=_viaje
    //accediendo a la instancia de Firestore
    private val user = FirebaseAuth.getInstance().currentUser
    private var emailUser: String? = null
    private val db = FirebaseFirestore.getInstance()


    fun sendData(travel: Travel, context: Context, resources: Resources) {
        db.collection("e-Tracker")
            .add(travel)
            .addOnSuccessListener {
                Log.d("Enviodata", "$travel")
                Toast.makeText(
                    context,
                    resources.getString(R.string.register_complete),
                    Toast.LENGTH_SHORT
                ).show()
                //homeTravelFragment(HomeTravelFragment())
                Log.i("BUSCANDOID", " el nuevo id del viaje es: ${it.id}")
                _isDataSent.value = it.id
            }
            .addOnFailureListener { e -> Log.w("Error", "$e") }
    }

    fun getData(id:String) {
        var viaje: MutableList<Travel>
        val docRef = db.collection("e-Tracker")
        val query: Query = docRef.whereEqualTo(FieldPath.documentId(), id)
        query.get().addOnSuccessListener { documentSnapshot ->
            viaje = documentSnapshot.toObjects(Travel::class.java)
            _viaje.value = viaje
        }
    }

    fun updateData(travel: Travel, id: String, context: Context, resources: Resources){
        db.collection("e-Tracker").document(id)
            .set(travel) //Realiza el seteo de la data en firebase
            .addOnSuccessListener {
                Log.d("Enviodata", "$travel")
                Toast.makeText(context,
                    resources.getString(R.string.register_update), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.w("Error", "$e") }
    }

    fun getTravel(id:String){
        db.collection("e-Tracker").document(id).get().addOnSuccessListener {
            val detailsViaje = it.toObject(Travel::class.java)
            _isActive.value = detailsViaje!!.active

        }
    }
}
