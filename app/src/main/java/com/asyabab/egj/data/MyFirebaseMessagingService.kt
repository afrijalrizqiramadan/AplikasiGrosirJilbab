package com.asyabab.egj.data

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("TAG", "The token refreshed:$token")
    }

}