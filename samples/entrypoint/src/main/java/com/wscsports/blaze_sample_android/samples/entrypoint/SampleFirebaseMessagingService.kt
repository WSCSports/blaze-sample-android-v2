package com.wscsports.blaze_sample_android.samples.entrypoint

import android.util.Log
import com.blaze.blazesdk.shared.BlazeSDK
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Sample of [FirebaseMessagingService] for handling push notifications:
 * [onMessageReceived] is triggered when the app is in the foreground.
 * Otherwise, notification data is passed via the Activity's intent.data.
 * Note: Ensure you add your own google-services.json file to the app module.
 */
class SampleFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("appDebug", "SampleFirebaseMessagingService: onNewToken: token = $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        // Blaze payload should arrive in the data field
        if (message.data.isNotEmpty()) {
            if (BlazeSDK.canHandlePushNotification(message.data)) {
                BlazeSDK.handlePushNotificationPayload(message.data)
            } else {
                // Message data payload is not for BlazeSDK
            }
        }
    }

}