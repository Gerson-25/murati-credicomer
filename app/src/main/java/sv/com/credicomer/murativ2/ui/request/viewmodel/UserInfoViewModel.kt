package com.example.creditscomer.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.uttampanchasara.pdfgenerator.CreatePdf
import sv.com.credicomer.murativ2.SharedPreferencesContainer
import sv.com.credicomer.murativ2.ui.request.model.GeneralInfoModel
import sv.com.credicomer.murativ2.utils.PdfCreator
import java.io.Serializable

class UserInfoViewModel(application: Application): BaseViewModel(application) {

    private var db = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance().reference
    private val waterReference = storage.child("credit_request/${System.currentTimeMillis()}_water_receip.png")
    private val energyReference = storage.child("credit_request/${System.currentTimeMillis()}_energy_receip.png")
    private val pdfReference = storage.child("credit_request/${System.currentTimeMillis()}_pdf.pdf")
    private val debtFormReference = storage.child("credit_request/${System.currentTimeMillis()}_debt_form.png")

    var _userInfo = MutableLiveData<Map<String,String?>>()
    val userInfo:LiveData<Map<String,String?>>
    get() = _userInfo

    private var _selectedDate = MutableLiveData<String>()
    val selectedDate:LiveData<String>
    get() = _selectedDate

    private val COLLECTION = "credit_request"

    private var sherPref = SharedPreferencesContainer(getApplication())

    var _location = MutableLiveData<String>()
    val location: LiveData<String>
        get() = _location

    var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    var _requestList = MutableLiveData<MutableList<GeneralInfoModel>>()
    val requestList: LiveData<MutableList<GeneralInfoModel>>
        get() = _requestList

    private var _pageScroll = MutableLiveData<Int>()
    val pageScroll: LiveData<Int>
        get() = _pageScroll

    private var _uploadFiles = MutableLiveData<Int>()
    val uploadFiles: LiveData<Int>
        get() = _uploadFiles

    var _personalInfo = MutableLiveData<HashMap<String,Serializable>>()
    val personalInfo: LiveData<HashMap<String,Serializable>>
        get() = _personalInfo

    var _pdfUriString = MutableLiveData<String>()
    val pdfUriString: LiveData<String>
        get() = _pdfUriString

    var _waterUriString = MutableLiveData<String>()
    val waterUriString: LiveData<String>
        get() = _waterUriString

    var _energyUriString = MutableLiveData<String>()
    val energyUriString: LiveData<String>
        get() = _energyUriString

    var _debtFormUriString = MutableLiveData<String>()
    val debtFormUriString: LiveData<String>
        get() = _debtFormUriString

    fun changePosition(position:Int){
        _pageScroll.value = position
    }



    fun saveLocation(location:String){
        _location.value = location
    }

    fun getName() = sherPref.getName()
    fun getId() = sherPref.getId()
    fun getEmail() = sherPref.getEmail()

    fun saveName(name:String){
        sherPref.saveName(name)
    }

    fun getCreditRequests(email:String){
        db.collection("credit_request").whereEqualTo("correo", email).get().addOnSuccessListener {
            val requests = it.toObjects(GeneralInfoModel::class.java)
            _requestList.value = requests
        }
    }

    fun initUploadCounter(){
        _uploadFiles.value = 0
    }



    fun sendInfo(myInfo:GeneralInfoModel){
        val generalInfo =
            hashMapOf(
                "fecha_solicitud" to myInfo.fecha_solicitud,
                "correo" to myInfo.correo,
                "codigo" to myInfo.codigo,
                "nombre" to myInfo.nombre,
                "telefono" to myInfo.telefono,
                "municipio" to myInfo.municipio,
                "departamento" to myInfo.departamento,
                "domicilio" to myInfo.domicilio,
                "ubicacion" to myInfo.ubicacion,
                "type" to myInfo.tipo,
                "fecha_ingreso" to myInfo.fecha_ingreso,
                "ingresos_variables" to myInfo.ingresos_variables,
                "garantia" to myInfo.garantia,
                "telefono_rf_uno" to myInfo.telefono_rf_uno,
                "nombre_rf_uno" to myInfo.nombre_rf_uno,
                "parentesco_rf_uno" to myInfo.parentesco_rf_uno,
                "telefono_rf_dos" to myInfo.telefono_rf_dos,
                "nombre_rf_dos" to myInfo.nombre_rf_dos,
                "parentesco_rf_dos" to myInfo.parentesco_rf_dos,
                "telefono_rp_uno" to myInfo.telefono_rp_uno,
                "nombre_rp_uno" to myInfo.nombre_rp_uno,
                "telefono_rp_dos" to myInfo.telefono_rp_dos,
                "nombre_rp_dos" to myInfo.nombre_rp_dos,
                "cantidad" to myInfo.cantidad,
                "plazo" to myInfo.plazo,
                "recibo_energia" to myInfo.energy_receip,
                "recibo_agua" to myInfo.water_receip,
                "recomendacion" to myInfo.recommendatio_letter,
                "monto_deuda" to myInfo.consolidar_deuda,
                "comentarios" to myInfo.comentarios,
                "seguro_deuda" to myInfo.formato_deuda,
                "id" to ""
            )

        _loading.value = true

        db.collection(COLLECTION).add(generalInfo)
            .addOnSuccessListener {
                _loading.value = false
            }

            .addOnFailureListener {
                _loading.value = false
            }

    }

    fun saveDebtForm(imageUri: Uri){
        debtFormReference.putFile(imageUri).addOnSuccessListener {
            debtFormReference.downloadUrl.addOnSuccessListener {uri ->
                _debtFormUriString.value = uri.toString()

                _uploadFiles.value = _uploadFiles.value?.plus(1)
            }
        }
    }

    fun getRequest(id:String){
        db.collection("credit_request").whereEqualTo("id", id).get().addOnSuccessListener {
            val requests = it.toObjects(GeneralInfoModel::class.java)
            _requestList.value = requests
        }
    }

    fun saveWaterReceip(imageUri:Uri){
        waterReference.putFile(imageUri).addOnSuccessListener {
            waterReference.downloadUrl.addOnSuccessListener {uri ->
               _waterUriString.value = uri.toString()
                _uploadFiles.value = _uploadFiles.value?.plus(1)
            }
        }
    }



    fun saveEnergyReceip(imageUri:Uri){
        energyReference.putFile(imageUri).addOnSuccessListener {
            energyReference.downloadUrl.addOnSuccessListener {uri ->
                _energyUriString.value = uri.toString()
                _uploadFiles.value = _uploadFiles.value?.plus(1)
            }
        }
    }

    fun saveFile(imageUri:Uri){
        pdfReference.putFile(imageUri).addOnSuccessListener {
            pdfReference.downloadUrl.addOnSuccessListener {uri ->
                _pdfUriString.value = uri.toString()
                _uploadFiles.value = _uploadFiles.value?.plus(1)
            }
        }
    }

}