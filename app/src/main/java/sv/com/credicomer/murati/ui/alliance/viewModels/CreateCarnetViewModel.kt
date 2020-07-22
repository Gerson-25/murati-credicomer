package sv.com.credicomer.murati.ui.alliance.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import sv.com.credicomer.murati.ui.alliance.models.UserCarnet

class CreateCarnetViewModel : ViewModel() {


    private var db= FirebaseFirestore.getInstance()
    private var auth= FirebaseAuth.getInstance()

    private var _isCarnet = MutableLiveData<MutableList<UserCarnet>>()
    val isCarnet: LiveData<MutableList<UserCarnet>>
    get() = _isCarnet
    private val user = auth.currentUser?.email.toString()

    private var _imRecord = MutableLiveData<Uri>()
    val imRecord: LiveData<Uri>
        get() = _imRecord

    private var _anim = MutableLiveData<Boolean>()
    val anim: LiveData<Boolean>
        get() = _anim

    private var _carnet = MutableLiveData<Boolean>()
    val carnet: LiveData<Boolean>
        get() = _carnet

    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    val imageRef =
        storageRef.child("alliance-image/carnets/${user}_image_carnet.jpg")//nombre del archivo a publicar


    fun getCarnet(){
        db.collection("users").whereEqualTo(FieldPath.documentId(),user).get().addOnSuccessListener {
            val userCarnet = it.toObjects(UserCarnet::class.java)

            _isCarnet.value = userCarnet
        }.addOnFailureListener {

        }
    }

    fun createCarnet(carnet:UserCarnet){
        db.collection("users").document(user).set(carnet).addOnSuccessListener {
            _anim.value = true

        }.addOnFailureListener{
            _anim.value = false
        }
    }

    fun getPhoto(imageDir: String,imageStorage:String) {
        if (imageStorage==""){

            val image = Uri.parse(imageDir)
            imageRef.putFile(image).addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    _imRecord.value = taskSnapshot.result



                }
            }
        }else {
            val storageRef: StorageReference =
                FirebaseStorage.getInstance().getReferenceFromUrl(imageStorage)
            storageRef //borra foto
                .delete()

            val image = Uri.parse(imageDir)
            imageRef.putFile(image).addOnSuccessListener {
                imageRef.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    _imRecord.value = taskSnapshot.result



                }
            }
        }
    }

    fun updateCarnet(userCarnet: UserCarnet){
        db.collection("users").document(user).update(
            "name",userCarnet.name,
            "collaboratorCod",userCarnet.collaboratorCod,
            "departmentName",userCarnet.departmentName
        ).addOnSuccessListener {
            _anim.value = true
        }.addOnFailureListener {
            _anim.value = false
        }

    }

    fun updateCarnetPhoto(userCarnet: UserCarnet){
        db.collection("users").document(user).update(
            "name",userCarnet.name,
            "collaboratorCod",userCarnet.collaboratorCod,
            "departmentName",userCarnet.departmentName,
            "carnetPhoto", userCarnet.carnetPhoto
        ).addOnSuccessListener {
            _anim.value = true
        }.addOnFailureListener {
            _anim.value = false
        }
    }


}