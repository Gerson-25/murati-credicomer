package sv.com.credicomer.murativ2

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharedPreferencesContainer {
    companion object{
        private const val USER_ID = "user id"
        private const val EMAIL = "email"
        private const val USER_NAME = "user name"
        private const val CREDIT_AMOUNT = "credit amount"
        private const val PROCESS_FINISHED = "finish"
        private const val TRANSPORTATION = "transportation"
        private const val TRAVEL = "travel"
        private const val ROOMS = "rooms"
        private const val ALLIANCE = "alliance"
        private var prefs: SharedPreferences? = null
        @Volatile private var instance: SharedPreferencesContainer? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesContainer = instance ?: synchronized(LOCK) {
            instance ?: buildHelper(context).also{
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharedPreferencesContainer{
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesContainer()
        }
    }

    //save user setting
    fun setTransportation(time:Boolean){
        prefs?.edit(commit = true){putBoolean(TRANSPORTATION, time)}
    }
    fun getTransportation() = prefs?.getBoolean(TRANSPORTATION, false)

    fun setTravels(time:Boolean){
        prefs?.edit(commit = true){putBoolean(TRAVEL, time)}
    }
    fun getTravels() = prefs?.getBoolean(TRAVEL, false)

    fun setRooms(time:Boolean){
        prefs?.edit(commit = true){putBoolean(ROOMS, time)}
    }
    fun getRooms() = prefs?.getBoolean(ROOMS, false)

    fun setAlliance(time:Boolean){
        prefs?.edit(commit = true){putBoolean(ALLIANCE, time)}
    }
    fun getAlliance() = prefs?.getBoolean(ALLIANCE, false)

    //end of setting


    //

    fun saveId(id:String){
        prefs?.edit(commit = true){putString(USER_ID, id)
        }
    }

    fun getId() = prefs?.getString(USER_ID, "")

    fun saveName(name:String){
        prefs?.edit(commit = true){putString(USER_NAME, name)}
    }

    fun getName() = prefs?.getString(USER_NAME, "")

    fun saveEmail(email:String){
        prefs?.edit(commit = true){putString(EMAIL, email)}
    }

    fun getEmail() = prefs?.getString(EMAIL, "")

}