package sv.com.credicomer.murati.ui.travel.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import sv.com.credicomer.murati.ui.travel.models.Travel
import java.lang.reflect.Array.get

class HomeViewModel : ViewModel(){

    private val FirebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val db= FirebaseFirestore.getInstance()
    private var collecRef: CollectionReference = db.collection("e-Tracker")

    private var _travelList = MutableLiveData <MutableList<Travel>>()
    val travelList: LiveData<MutableList<Travel>>
        get() = _travelList
    private var _oldTravel = MutableLiveData<String>()
    val oldTravel: LiveData<String>
    get() = _oldTravel




    fun setUpRecyclerView(){
        /*val query: Query =collecRef
            .orderBy("finishDate", Query.Direction.DESCENDING)
            .whereEqualTo("active", false)
            .whereEqualTo("emailUser", FirebaseUser!!.email)
        query.*/
        db.collection("e-Tracker").whereEqualTo("active", false).get().addOnSuccessListener {

            //Log.d("ITSNAP", it.documents[0].id)
            if (it.documents.isNotEmpty()){
            //_oldTravel.value = it.documents[0].id
            _travelList.value = it.toObjects(Travel::class.java)
            Log.d("OPTIONS", "${_travelList.value}")
            }else{
                _travelList.value = mutableListOf()
            }
        }
            .addOnFailureListener{
                Log.d("ITSNAP","FALLO" )
            }
    }
}