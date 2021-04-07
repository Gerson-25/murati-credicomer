package sv.com.credicomer.murativ2.ui.profile.view.adapters

import sv.com.credicomer.murativ2.ui.profile.model.Recognition
import sv.com.credicomer.murativ2.ui.profile.model.UserCarnet

class RecognitionHandler {

    //verify if I am the sender or the receiver
    fun isMine(myEmail:String, recognition: Recognition): Boolean = myEmail == recognition.sender


    //get the sender information
    fun getSenderData(senderEmail:String, users:List<UserCarnet>):UserCarnet{
        users.filter {
            it.email == senderEmail
        }

        return users[0]
    }



}