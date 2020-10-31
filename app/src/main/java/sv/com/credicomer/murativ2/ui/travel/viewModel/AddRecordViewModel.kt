package sv.com.credicomer.murativ2.ui.travel.viewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import sv.com.credicomer.murativ2.constants.ADD_RECORD_FRAGMENT
import sv.com.credicomer.murativ2.constants.FIREBASE_TRAVEL_ID
import sv.com.credicomer.murativ2.ui.travel.models.Record
import timber.log.Timber


class AddRecordViewModel: ViewModel() {

    private var _isRecordSent = MutableLiveData<String>()
    val isRecordSent: LiveData<String>
        get() = _isRecordSent

    private var _imRecord = MutableLiveData<Uri>()
    val imRecord: LiveData<Uri>
        get() = _imRecord

    private var _anim = MutableLiveData<Boolean>()
    val anim: LiveData<Boolean>
        get() = _anim




    val firebaseDB = FirebaseFirestore.getInstance()
    val firestoreDB = FirebaseFirestore.getInstance()
    //FireStorage
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    val imageRef =
        storageRef.child("record-image/${System.currentTimeMillis()}_image_ticket.png")//nombre del archivo a publicar

    //INSTANCIA DE FIREBASE

    fun updateRecord(record: Record, travelId: String, recordId: String, context: Context) {



        firebaseDB.collection("e-Tracker").document(travelId).collection("record")
            .document(recordId)
            .update(
                "recordName", record.recordName,
                "recordDate", record.recordDate,
                "recordMount", record.recordMount,
                "recordCategory", record.recordCategory,
                "recordDescription", record.recordDescription,
                "recordDateLastUpdate", record.recordUpdateRegister
            )
            .addOnFailureListener {

                _anim.value = false

                Toast.makeText(context, "El record no pudo ser actualizado.", Toast.LENGTH_SHORT)
                    .show()

            }
            .addOnSuccessListener {
                Toast.makeText(context, "El record ha sido actualizado.", Toast.LENGTH_SHORT).show()

                _anim.value = true

            }
    }

    fun getPhoto(imageDir: String,imageStorage:String) {
        if (imageStorage==""){
            Timber.d("GET_PHOTO %s", imageDir)
            val image = Uri.parse(imageDir)
            imageRef.putFile(image).addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    _imRecord.value = taskSnapshot.result

                    Timber.i("$ADD_RECORD_FRAGMENT%s", "SI LLEGO HASTA AQUI")
                    Timber.d("ES_EL_TASKSNAPSHOT %s", "${taskSnapshot.result}")

                }
            }
        }else {
            val storageRef: StorageReference =
                FirebaseStorage.getInstance().getReferenceFromUrl(imageStorage)
            storageRef //borra foto
                .delete()
            Timber.d("PHOTO_DELETE %s", "lo borro")
            val image = Uri.parse(imageDir)
            imageRef.putFile(image).addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    _imRecord.value = taskSnapshot.result

                    Timber.i("$ADD_RECORD_FRAGMENT%s", "SI LLEGO HASTA AQUI")
                    Timber.d("ES_EL_TASKSNAPSHOT %s", "${taskSnapshot.result}")

                }
            }
        }
    }

    fun updatePhoto(travelId: String, record: Record, recordId: String, context: Context) {

        firebaseDB.collection("e-Tracker").document(travelId).collection("record")
            .document(recordId)
            .update(
                "recordName", record.recordName,
                "recordDate", record.recordDate,
                "recordMount", record.recordMount,
                "recordCategory", record.recordCategory,
                "recordPhoto", record.recordPhoto,
                "recordDescription", record.recordDescription,
                "recordDateLastUpdate", record.recordUpdateRegister
            )
            .addOnFailureListener {
                _anim.value = false

                Toast.makeText(context, "El record no pudo ser actualizado.", Toast.LENGTH_SHORT)
                    .show()


                Timber.i("$ADD_RECORD_FRAGMENT%s", "Error $it")
            }
            .addOnSuccessListener {
                Toast.makeText(context, "El record ha sido actualizado.", Toast.LENGTH_SHORT).show()

                _anim.value = true

                Timber.i(
                    "$ADD_RECORD_FRAGMENT%s", "Registro agregado existosamente con ID de Viaje $FIREBASE_TRAVEL_ID"
                )
            }
    }

    fun sendPhoto(record: Record, travelId: String, context: Context) {


        firestoreDB.collection("e-Tracker").document(travelId).collection("record")
            .add(record)
            .addOnFailureListener {
                _anim.value = false
                Toast.makeText(context, "La creación del registro ha fallado", Toast.LENGTH_SHORT)
                    .show()
                Timber.e("$ADD_RECORD_FRAGMENT%s", "Error en $it")
            }
            .addOnSuccessListener {
                _anim.value = true

                Toast.makeText(context, "Registro creado exitosamente.", Toast.LENGTH_SHORT).show()

            }


    }





}