package sv.com.credicomer.murativ2

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import sv.com.credicomer.murativ2.constants.*

class MainViewModel(application: Application) : BaseViewModel(application) {

    var travelId = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var isActive = MutableLiveData<Boolean>()

    private var shPref = SharedPreferencesContainer(getApplication())

    private var auth = FirebaseAuth.getInstance()

    private var _allianceCollectionPath = MutableLiveData<String>()

    private var _allianceSubCollectionPath = MutableLiveData<String>()

    private var _roomCollectionPath = MutableLiveData<String>()

    private var _roomSubCollectionPath= MutableLiveData<String>()

    val allianceCollectionPath: LiveData<String>
        get() = _allianceCollectionPath

    val allianceSubCollectionPath: LiveData<String>
        get() = _allianceSubCollectionPath

    val roomCollectionPath : LiveData<String>
        get() = _roomCollectionPath

    val roomSubCollectionPath:LiveData<String>
        get() = _roomSubCollectionPath

    private var _email = MutableLiveData<String>()
    val email:LiveData<String>
    get() = _email

    private var _roomState = MutableLiveData<Boolean>()
    val roomState:LiveData<Boolean>
        get() = _roomState



    fun getCredentials() {

        val email = auth.currentUser?.email.toString()
        _email.value = email

        if (email.contains(UNICOMER_DOMAIN, ignoreCase = true)) {

            _allianceCollectionPath.value = UNICOMER_ALLIANCE_PATH
            _allianceSubCollectionPath.value = UNICOMER_PROMOTION_PATH
            _roomCollectionPath.value = UNICOMER_ROOMS_PATH
            _roomSubCollectionPath.value = UNICOMER_ROOMS_RESERVATIONS_PATH
        } else {


            _allianceCollectionPath.value = CREDICOMER_ALLIANCE_PATH
            _allianceSubCollectionPath.value = CREDICOMER_PROMOTION_PATH
            _roomCollectionPath.value = CREDICOMER_ROOMS_PATH
            _roomSubCollectionPath.value = CREDICOMER_ROOMS_RESERVATIONS_PATH
        }


    }

    fun getEmailStatic(): String{
        return auth.currentUser?.email.toString()
    }

    fun changeProfile(user_email: String){
        _email.value = user_email
    }

    fun setRoomsOption(state: Boolean){
        shPref.setRooms(state)
        _roomState.value = state
    }


}