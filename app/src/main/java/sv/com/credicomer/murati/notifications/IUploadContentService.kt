package sv.com.credicomer.murati.notifications

import android.net.Uri
import androidx.core.app.NotificationManagerCompat
import sv.com.credicomer.murati.ui.travel.models.Record

interface IUploadContentService {
     enum class RecordEvent  {
          NEW_RECORD,UPDATE_RECORD_NO_PHOTO,UPDATE_RECORD_NEW_PHOTO
     }
     fun newRecord(record: Record, travelId:String, imageUri: Uri, notificationCompat: NotificationManagerCompat)
     fun updateRecordNoPhoto(record:Record, travelId:String,  notificationCompat: NotificationManagerCompat)
     fun updateRecordNewPhoto(record:Record, travelId:String, imageUri:Uri, notificationCompat: NotificationManagerCompat)
}