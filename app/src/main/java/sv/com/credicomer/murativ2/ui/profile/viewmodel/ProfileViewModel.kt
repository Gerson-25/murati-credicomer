package sv.com.credicomer.murativ2.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.ui.profile.model.Acknowledge
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet

class ProfileViewModel:ViewModel() {
    val db = FirebaseFirestore.getInstance()

    var _recognitions = MutableLiveData<MutableList<Acknowledge>>()
    val recognitions:LiveData<MutableList<Acknowledge>>
    get() = _recognitions

    var _messageIsSent = MutableLiveData<Boolean>()
    val messageIsSent:LiveData<Boolean>
        get() = _messageIsSent



    var _messages = MutableLiveData<MutableList<Recognition>>()
    val messages:LiveData<MutableList<Recognition>>
        get() = _messages

    var _myUser = MutableLiveData<UserCarnet>()
    val myUser:LiveData<UserCarnet>
    get() = _myUser

    var _users = MutableLiveData<List<UserCarnet>>()
    val users:LiveData<List<UserCarnet>>
        get() = _users

    fun getRecognitions(email:String){
        db.collection("users").document(email)
            .collection("recognitions")
            .addSnapshotListener { querySnapshot, ex ->
                _recognitions.value = querySnapshot!!.toObjects(Acknowledge::class.java)
            }
    }

    fun getAllMessages(){
        db.collection("recognitions").whereEqualTo("status", "aprovado").
        addSnapshotListener { value, error ->
            val message_list = value!!.toObjects(Recognition::class.java)
            _messages.value = message_list
        }
    }

    fun getPendingMessages(email: String){
        db.collection("recognitions").whereEqualTo("sender", email).whereEqualTo("status", "pendiente").
            addSnapshotListener { value, error ->
                val message_list = value!!.toObjects(Recognition::class.java)
                _messages.value = message_list
            }
    }

    fun getMessages(email: String){
        var messagesContainer = mutableListOf<Recognition>()
        db.collection("recognitions").whereEqualTo("sender", email).whereEqualTo("status", "aprovado").
        addSnapshotListener { value, error ->

            messagesContainer.addAll(value!!.toObjects(Recognition::class.java))
            db.collection("recognitions").whereArrayContains("receiver", email).whereEqualTo("status", "aprovado")
                .addSnapshotListener { value2, error2 ->
                    messagesContainer.addAll(value2!!.toObjects(Recognition::class.java))
                    _messages.value = messagesContainer
                }


        }
    }

    fun sendMessages(recognition: Recognition){
        db.collection("recognitions").add(recognition).addOnSuccessListener {
            _messageIsSent.value = true
        }.addOnFailureListener {
            _messageIsSent.value = false
        }
    }

    fun getMyUser(email:String){
        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener { querySnapshot ->
            var users = querySnapshot.toObjects(UserCarnet::class.java)
            _myUser.value = users[0]
        }.addOnFailureListener {
        }
    }

    fun getUsers(){
        db.collection("users").get().addOnSuccessListener { querySnapshot ->
            val users = querySnapshot.toObjects(UserCarnet::class.java)
            _users.value = users
        }.addOnFailureListener {
        }
    }

    fun likeMessage(messageId: String, email:List<String>){
        db.collection("recognitions").document(messageId).update("like", email).addOnSuccessListener {

        }
    }
}