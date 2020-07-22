package sv.com.credicomer.murati.ui.travel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sv.com.credicomer.murati.FileUtil
import java.io.File
import java.io.InputStream


val db = FirebaseFirestore.getInstance()
var travelRef: CollectionReference = db.collection("e-Tracker")

fun finishTravel(idTravel:String, context:Context){
    travelRef
        .document(idTravel)
        .update("active", false)
        .addOnSuccessListener {

            Toast.makeText(context, "Viaje finalizado", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
                e ->
        }
}

fun File.copyInputStreamToFile(inputStream: InputStream) {
    this.outputStream().use { fileOut ->
        inputStream.copyTo(fileOut)
    }
}

fun InputStream.toFile(path: String) {
    File(path).outputStream().use { this.copyTo(it) }
}

suspend fun getImageFile(context: Context,imagePath:String):File =
    withContext(Dispatchers.IO) {

         FileUtil.from(context, Uri.parse(imagePath))

    }
