package sv.com.credicomer.murati.ui.alliance.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import sv.com.credicomer.murati.ui.alliance.models.EstablishmentFS

class EstablishmentsViewModel : ViewModel() {

    private var db= FirebaseFirestore.getInstance()

    private var _establishmentsFS = MutableLiveData<MutableList<EstablishmentFS>>()

    val establishmentFS: LiveData<MutableList<EstablishmentFS>>
        get() = _establishmentsFS

    init {
        val settings= FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings=settings
    }

    fun getFireStoreEstablishments(category:String,collectionPath:String){

        // var allEstablishment= mutableListOf<EstablishmentFS>()

        db.collection(collectionPath).document(category).collection("establishments").whereEqualTo("estado", true).get()
            .addOnSuccessListener {snap->

                val obj=snap.toObjects(EstablishmentFS::class.java)

                _establishmentsFS.value=obj




            }


    }
}
