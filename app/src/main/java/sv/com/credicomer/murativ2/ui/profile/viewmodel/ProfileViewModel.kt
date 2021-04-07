package sv.com.credicomer.murativ2.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import sv.com.credicomer.murativ2.ui.profile.model.Acknowledge
import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet

class ProfileViewModel:ViewModel() {
    val db = FirebaseFirestore.getInstance()

    var _recognitions = MutableLiveData<MutableList<Acknowledge>>()
    val recognitions:LiveData<MutableList<Acknowledge>>
    get() = _recognitions

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

    fun getMessages(email: String){
        db.collection("users").document(email).collection("messages").
            addSnapshotListener { value, error ->
                val message_list = value!!.toObjects(Recognition::class.java)
                _messages.value = message_list
            }
        /*db.collection("users").document(email).collection("messages").
                get().addOnSuccessListener {
            val message_list = it.toObjects(Message::class.java)
            _messages.value = message_list
        }*/
    }

    fun sendMessages(recognition: Recognition, sender:String, receiver:String){
        //save as a sender
        db.collection("users").document(sender).collection("messages").add(recognition).addOnSuccessListener {
        }.addOnFailureListener {

        }
        //save as a receiver
        db.collection("users").document(receiver).collection("messages").add(recognition).addOnSuccessListener {
        }.addOnFailureListener {

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

    fun likeMessage(email: String, messageId: String, receiver: String){
        db.collection("users").document(email).collection("messages").whereEqualTo("id", messageId).get().addOnSuccessListener {
            it.forEach { documentSnapshot ->
                documentSnapshot.reference.update("like", true)
            }
        }
        db.collection("users").document(receiver).collection("messages").whereEqualTo("id", messageId).get().addOnSuccessListener {
            it.forEach { documentSnapshot ->
                documentSnapshot.reference.update("like", true)
            }
        }
    }
}