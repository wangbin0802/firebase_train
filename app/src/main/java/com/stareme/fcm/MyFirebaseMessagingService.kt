package com.stareme.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by wangbin on 11/6/21.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FirebaseService", "onMessageReceived message:${remoteMessage.messageId}, ${remoteMessage.data.values}")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseService", "onNewToken:$token")
    }
}