package sv.com.credicomer.murati.ui.travel.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailRecordViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    //Firebase reference
    private val db = FirebaseFirestore.getInstance()
    private var travelRef: CollectionReference = db.collection("e-Tracker")



fun deleteRecord(recordPhoto: String, idTravel:String, idRecord:String){

    val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(recordPhoto)
    storageRef //borra foto
        .delete()
    Log.i("PHOTO", "la url de la foto es: $recordPhoto")
    travelRef
        .document(idTravel)
        .collection("record")
        .document(idRecord)
        .delete()
        .addOnSuccessListener {
            Log.i("ELIMINAR", "el delete fue un exito de este id: $idRecord, y el del viaje: $idTravel")

        }
        .addOnFailureListener { e -> Log.w("ERROR_DOC_DELETE", "Error deleting document", e) }
    }
}
