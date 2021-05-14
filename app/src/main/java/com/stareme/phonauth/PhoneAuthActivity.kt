package com.stareme.phonauth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.stareme.firebase.R
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : AppCompatActivity() {
    private lateinit var verifyCodeEdit: EditText
    private lateinit var verifyStart: Button
    private lateinit var sendSmsCode: Button

    private lateinit var auth: FirebaseAuth

    private var phoneNumber = "+1 658-435-7416"
//    private var phoneNumber = "+65 84357416"
    private var credential: PhoneAuthCredential? = null
    private var storedVerificationId: String? = null
    private var refreshToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)
        setSupportActionBar(findViewById(R.id.toolbar))

        auth = FirebaseAuth.getInstance()

        verifyCodeEdit = findViewById(R.id.verifyCode)
        verifyCodeEdit.hint = "输入短信验证码"

        sendSmsCode = findViewById(R.id.sendSmsCode)
        sendSmsCode.setOnClickListener {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

        verifyStart = findViewById(R.id.verfiy)
        verifyStart.setOnClickListener {
            if (storedVerificationId != null && verifyCodeEdit.text != null) {
                credential = PhoneAuthProvider.getCredential(storedVerificationId!!,
                    verifyCodeEdit.text.toString()
                )
                signInWithPhoneAuthCredential(credential!!)
            }
        }
    }

    // [START phone_auth_callbacks]
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId, refresh token:$token")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            refreshToken = token
        }
    }
    // [END phone_auth_callbacks]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Log.d(TAG, "firebase user id:${user?.uid}, ${user?.phoneNumber}")
                    user?.getIdToken(true)?.addOnCompleteListener {
                        Log.d(TAG, "firebase user token:${it.result?.token}")
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    // [END resend_verification]


    companion object {
        const val TAG = "PhoneAuthActivity"
    }
}