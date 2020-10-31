package sv.com.credicomer.murativ2.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel:ViewModel() {
    val db = FirebaseFirestore.getInstance()

    var _recognitions = MutableLiveData<MutableList<Acknowledge>>()
    val recognitions:LiveData<MutableList<Acknowledge>>
    get() = _recognitions

    fun getRecognitions(){
        var ackList = arrayListOf<Acknowledge>()
        db.collection("users").document("gmisaelbeltran@gmail.com")
            .collection("recognitions")
            .addSnapshotListener { querySnapshot, ex ->
                _recognitions.value = querySnapshot!!.toObjects(Acknowledge::class.java)
            }
    }
}