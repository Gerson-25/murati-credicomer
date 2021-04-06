package sv.com.credicomer.murativ2.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import sv.com.credicomer.murativ2.MainActivity
import sv.com.credicomer.murativ2.R
import timber.log.Timber


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]




        sendNotification(remoteMessage)

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.d("$TAG%s", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Timber.d("$TAG%s", "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Timber.d("$TAG%s", "Message Notification Body: ${it.body}")
            Timber.d("$TAG%s", "Message Notification Body: ${it.title}")

        }
        remoteMessage.data.let {

            Timber.d("$TAG%s", "Message Notification dATA: ${it.values}")
            Timber.d("$TAG%s", "Message Notification data: $it")

        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Timber.d("$TAG%s", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(applicationContext).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Timber.d(TAG+"%s", "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Timber.d("$TAG%s", "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: RemoteMessage) {
        val module=messageBody.data.getValue("module")
       // val intent = Intent(this, MainActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      //  val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
         //      PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        lateinit var notificationBuilder:NotificationCompat.Builder

        when(module){

            "ride"->{
              val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.murati_navigation)
                    .setDestination(R.id.nav_map)
                    .createPendingIntent()

                Timber.d("NOTIFICATION-TYPE %s","type of -> $module")
                notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.drawable.ic_ride_final)
                    .setContentTitle(messageBody.notification?.title)
                    .setContentText(messageBody.notification?.body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(defaultSoundUri)
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(messageBody.notification?.body))
                    .setContentIntent(pendingIntent)
            }
            "alliance"->{
                val idPromo= messageBody.data["idPromo"]

               val pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.murati_navigation)
                    .setDestination(R.id.nav_alliance)
                    .setArguments(bundleOf("id_promotion" to idPromo))
                    .createPendingIntent()
                Timber.d("NOTIFICATION-TYPE %s","type of -> $module")
                Timber.d("NOTIFICATION-ALLIANCE %s","ALLIANCE ID -> $idPromo")
                notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_alliance_final)
                    .setContentTitle(messageBody.notification?.title)
                    .setContentText(messageBody.notification?.body)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(defaultSoundUri)
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(messageBody.notification?.body))
                    .setContentIntent(pendingIntent)
            }



        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }



    companion object {

        private const val TAG = "MyFirebaseMsgService"
        private const val ID_NOTIFICATIONS =123
    }
}
