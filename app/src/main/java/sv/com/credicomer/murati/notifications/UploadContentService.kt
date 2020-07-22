package sv.com.credicomer.murati.notifications


import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import sv.com.credicomer.murati.MainActivity
import sv.com.credicomer.murati.R
import sv.com.credicomer.murati.constants.ADD_RECORD_FRAGMENT
import sv.com.credicomer.murati.constants.FIREBASE_TRAVEL_ID
import sv.com.credicomer.murati.notifications.IUploadContentService.RecordEvent
import sv.com.credicomer.murati.ui.travel.models.Record
import timber.log.Timber


class UploadContentService : IntentService("UploadContentService"),IUploadContentService {

    private val PROGRESS_MAX = 100
    private val PROGRESS_CURRENT = 0
    private lateinit var channelId:String
    private val notificationId = 555
    private lateinit var notification:NotificationCompat.Builder
    private val fireStore = FirebaseFirestore.getInstance()
    private var storage = FirebaseStorage.getInstance().reference
    private val imageReference = storage.child("record-image/${System.currentTimeMillis()}_image_ticket.png")


    override fun onHandleIntent(intent: Intent?) {
        channelId = getString(R.string.record_notification_channel_id)
        val pendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

        try {
            val record = intent?.getParcelableExtra<Record>("record")
            val travelId = intent?.getStringExtra("travelId")
            val event=intent?.getSerializableExtra("event")
            val imageDirectory = intent?.getStringExtra("photo")
            var imageUri:Uri?=null
            if (event==RecordEvent.UPDATE_RECORD_NEW_PHOTO || event== RecordEvent.NEW_RECORD) {
                 imageUri = Uri.parse(imageDirectory)
            }



            Timber.d("IMAGE URL %s", imageUri.toString())
            Timber.d("RECORD %s", "$record")

             notification = NotificationCompat.Builder(this, channelId).apply {
                setSmallIcon(R.drawable.ic_etracker_logo)
                setContentTitle(getText(R.string.notification_record_upload_title))
                setContentText(getText(R.string.notification_upload_message))
                priority = NotificationCompat.PRIORITY_LOW
                setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
                setContentIntent(pendingIntent)

            }

            NotificationManagerCompat.from(this).apply {

                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_LOW
                    )
                    notificationManager.createNotificationChannel(channel)

                }
                notificationManager.notify(notificationId, notification.build())

                when(event){

                    RecordEvent.NEW_RECORD ->{
                        newRecord(record!!,travelId!!,imageUri!!,this)
                    }
                    RecordEvent.UPDATE_RECORD_NEW_PHOTO ->{
                        updateRecordNewPhoto(record!!,travelId!!,imageUri!!,this)
                    }
                    RecordEvent.UPDATE_RECORD_NO_PHOTO ->{
                        notification.setContentText("Upload in progress")
                            .setProgress(0, 0, true)
                        notify(notificationId, notification.build())
                        updateRecordNoPhoto(record!!,travelId!!,this)
                    }


                }





            }


            //startForeground(999,notification.build())
        } catch (e: InterruptedException) {

            Thread.currentThread().interrupt()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Uploading your information", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }

     override fun newRecord(record:Record, travelId:String, imageUri:Uri, notificationCompat: NotificationManagerCompat){
        imageReference.putFile(imageUri).addOnSuccessListener {
            imageReference.downloadUrl.addOnSuccessListener {

                record.recordPhoto = it.toString()

                fireStore.collection("e-Tracker").document(travelId)
                    .collection("record")
                    .add(record).addOnSuccessListener {

                        notification.setContentText("Upload complete")
                            .setProgress(0, 0, false)
                       notificationCompat.notify(notificationId, notification.build())
                    }

            }


        }.addOnProgressListener {

            val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
            Timber.d("progress long %s", progress)
            Timber.d("progress int %s", progress.toInt())
            notification.setProgress(PROGRESS_MAX, progress.toInt(), false)
            notificationCompat.notify(notificationId, notification.build())
            Timber.d("PERCENTAGE %s", progress)
        }
    }

     override fun updateRecordNoPhoto(record:Record, travelId:String, notificationCompat: NotificationManagerCompat){
        fireStore.collection("e-Tracker").document(travelId).collection("record")
            .document(record.recordId.toString())
            .update(
                "recordName", record.recordName,
                "recordDate", record.recordDate,
                "recordMount", record.recordMount,
                "recordCategory", record.recordCategory,
                "recordDescription", record.recordDescription,
                "recordDateLastUpdate", record.recordUpdateRegister
            )
            .addOnSuccessListener {
                notification.setContentText("Upload complete")
                    .setProgress(0, 0, false)
                notificationCompat.notify(notificationId, notification.build())
            }.addOnFailureListener{

                Timber.d("the upload failed %s","${it.message}")
            }
    }

     override fun updateRecordNewPhoto(record:Record, travelId:String, imageUri:Uri, notificationCompat: NotificationManagerCompat){

       FirebaseStorage.getInstance().getReferenceFromUrl(record.recordPhoto.toString()).delete()
         Timber.d("PHOTO_DELETE %s", "photo Deleted")

         imageReference.putFile(imageUri).addOnSuccessListener {
             imageReference.downloadUrl.addOnSuccessListener {

                 record.recordPhoto = it.toString()

                 fireStore.collection("e-Tracker").document(travelId).collection("record")
                     .document(record.recordId.toString())
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

                         Timber.i("RECORD FAILED %s", "Error $it")
                     }
                     .addOnSuccessListener {
                         notification.setContentText("Upload complete")
                             .setProgress(0, 0, false)
                         notificationCompat.notify(notificationId, notification.build())
                         Timber.i(
                             "$ADD_RECORD_FRAGMENT%s", "Registro agregado existosamente con ID de Viaje $FIREBASE_TRAVEL_ID"
                         )
                     }

             }


         }.addOnProgressListener {

             val progress: Double = 100.0 * it.bytesTransferred / it.totalByteCount
             Timber.d("progress long %s", progress)
             Timber.d("progress int %s", progress.toInt())
             notification.setProgress(PROGRESS_MAX, progress.toInt(), false)
             notificationCompat.notify(notificationId, notification.build())
             Timber.d("PERCENTAGE %s", progress)
         }
    }
}