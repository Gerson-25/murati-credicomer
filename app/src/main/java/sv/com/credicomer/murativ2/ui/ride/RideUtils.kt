package sv.com.credicomer.murativ2.ui.ride

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import sv.com.credicomer.murativ2.ui.ride.adapters.AdapterFSItem
import sv.com.credicomer.murativ2.ui.ride.models.ReservationFS
import timber.log.Timber


fun subscribeTopicNotifications(topic: String) {

    Timber.d("NOTIFICATION", "Subscribing to $topic topic")


    FirebaseMessaging.getInstance().subscribeToTopic(topic)
        .addOnCompleteListener { task ->
            var msg = "SUSCRIPTION SUCCESS"
            if (!task.isSuccessful) {
                msg = "SUSCRIPTION FAILED"
            }
            Timber.d("NOTIFICATION", msg)

        }


}

fun unsubscribeTopicNotifications(topic: String) {

    Timber.d("NOTIFICATION", "Unsubscribing to $topic topic")
    // [START subscribe_topics]
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        .addOnCompleteListener { task ->
            var msg = "UNSUSCRIPTION SUCCESS"
            if (!task.isSuccessful) {
                msg = "UNSUSCRIPTION FAILED"
            }
            Timber.d("NOTIFICATION", msg)
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    // [END subscribe_topics]


}

fun generateBitmapDescriptorFromRes(
    context: Context, resId: Int
): BitmapDescriptor {
    val drawable = ContextCompat.getDrawable(context, resId)
    drawable!!.setBounds(
        0,
        0,
        drawable.intrinsicWidth,
        drawable.intrinsicHeight
    )
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

private var token: String? = null
@SuppressLint("TimberArgCount")
fun getToken(): String {


    FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.w(task.exception, "TOKEN", "getInstanceId failed")
                return@OnCompleteListener
            }

            // Get new Instance ID token
            token = task.result?.token

            // Log and toast
            val msg = "the generated token -> $token"
//
            Timber.d("TOKEN", msg)
            // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

        })
    Log.d("TAG", "this is the token" + token.toString())
    // [END retrieve_current_token]
    return token.toString()
}




fun getDateJoda():String{

    val current =DateTime.now()
    val formatter = DateTimeFormat.forPattern("dd-MM-yyyy")

    return current.toString(formatter)
}

fun updateReservationsFSList(list: List<AdapterFSItem<ReservationFS>>): List<AdapterFSItem<ReservationFS>> {

    val morningList = list.filter { it.value?.schedule_time == "morning" && it.value.round_status=="available"}.toMutableList()
    val eveningList = list.filter { it.value?.schedule_time == "evening" && it.value.round_status=="available"}.toMutableList()
    val afternoonList = list.filter { it.value?.schedule_time == "afternoon" && it.value.round_status=="available"}.toMutableList()

val formattedList:MutableList<AdapterFSItem<ReservationFS>> = mutableListOf()

    //victor header

    formattedList.add(AdapterFSItem<ReservationFS>(null, TYPE_VICTOR_HEADER))
    formattedList.add(AdapterFSItem<ReservationFS>(null, TYPE_MORNING_HEADER))
    formattedList.addAll(morningList)
    formattedList.add(AdapterFSItem<ReservationFS>(null, TYPE_EVENING_HEADER))
    formattedList.addAll(eveningList)
    formattedList.add(AdapterFSItem<ReservationFS>(null, TYPE_AFTERNOON_HEADER))
    formattedList.addAll(afternoonList)


    return formattedList


}
