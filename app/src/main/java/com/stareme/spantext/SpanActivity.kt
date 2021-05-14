package com.stareme.spantext

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.michatapp.michat_authorization.factory.ApiFactory
import com.stareme.firebase.R
import com.stareme.injection.*
import com.michatapp.michat_authorization.api.IAppStatusListener


class SpanActivity : AppCompatActivity() {
    @AutoWired private var userInfo: UserInfo? = null
    @BindView(R.id.span_tv) var spanTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_span)

        AutoWiredProcess.bind(this)
        ButterKnifeProcess.bind(this)

        Log.d("SpanActivity", "onCreate user info:${userInfo}")

        val stringBuilder = StringBuilder()
        val firstPartSpan = "This is first part"
        stringBuilder.append(firstPartSpan)
        val iconSpan = IconTextSpan(this, R.color.colorPrimary, "10")
        stringBuilder.append(" ")
        val lastPartSpan = "last part"
        stringBuilder.append(lastPartSpan)
        val spannableString = SpannableString(stringBuilder.toString())
        val spanIndex = firstPartSpan.length
        spannableString.setSpan(
            iconSpan,
            spanIndex,
            spanIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        spanTv.text = spannableString
        spanTv?.text = getSmallWidth().toString()

        findViewById<Button>(R.id.btn).setOnClickListener {
//            jumpToWhatsapp()
            getAppInfoApi()
        }


    }

    private fun getAppInfoApi() {
        val appInfoApi = ApiFactory.getAppInfoApi()
        appInfoApi.setAppStatusListener(this, object : IAppStatusListener {
            override fun onAppStatus(logStatus: Int) {
                Log.d("MainActivity", "onAppStatus:$logStatus")
                Toast.makeText(this@SpanActivity, "app login status:$logStatus", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun jumpToWhatsapp() {
        val sendIntent = Intent("android.intent.action.MAIN")
        sendIntent.action = Intent.ACTION_VIEW
        sendIntent.setPackage("com.whatsapp")
        val url = "https://api.whatsapp.com/send?phone=+6584936409&text=自动跳转到whatsapp会话内容"
        sendIntent.data = Uri.parse(url)
        if (sendIntent.resolveActivity(packageManager) != null) {
            startActivity(sendIntent)
        }
    }

    private fun getSmallWidth(): Float {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val widthPixels: Int = getScreenWidth(this)
        val density = dm.density
        return widthPixels / density
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }
}