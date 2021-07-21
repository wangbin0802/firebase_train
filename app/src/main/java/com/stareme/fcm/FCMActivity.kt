package com.stareme.fcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.stareme.firebase.R
import com.stareme.phonenumber.formatPhoneNumber

class FCMActivity : AppCompatActivity() {
    private var mIsOnMobileChangeEntered = false
    private val mPhoneNumberUtil = PhoneNumberUtil.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f_c_m)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("FCMActivity", "message toke:$token")
        })

    }
}