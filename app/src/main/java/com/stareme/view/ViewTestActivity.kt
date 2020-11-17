package com.stareme.view

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import com.stareme.firebase.R


/**
 * Created by wangbin on 20/10/20.
 */
class ViewTestActivity : AppCompatActivity() {
    private lateinit var customView: CustomView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_test_layout)
        customView = findViewById(R.id.custom_view)
        val button1 = findViewById<Button>(R.id.button1)
        val text = "<a href=\"zenxin://activity?page=a00010&\n" +
                "\n" +
                "url=xx&fullwindow=0\">\n" +
                "点击文案\n" +
                "\n" +
                "</a>"
        button1.text = Html.fromHtml(text)
        button1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                "market://details?id=com.cmcm.uangme&referrer=af_tranid%3Dk1IrAmfBx87gKQOhu-w9SA%26md5_mac%3D__MAC__%26af_c_id%3D__PID__%26md5_imei%3D__IMEI__%26os%3D__OS__%26af_adset_id%3D__UID__%26s2s%3D__S2S__%26pid%3Dwifimasterkey_int%26af_click_lookback%3D30d%26stime%3D__STIME__%26imei%3D__PLAIN_IMEI__%26android_id%3D__ANDROID_ID__%26af_adset%3D__UID__%26af_ad%3D__CGID__%26af_web_id%3D8f1e9518-abd6-4945-a57e-22b523c56482-c%26af_ad_id%3D__CGID__%26c%3D__PID__%26sid%3D__SID__"
                )
                setPackage("com.android.vending")
            }
            startActivity(intent)

        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(
            "ViewTestActivity",
            "onResume custom view size:${customView.measuredHeight}, ${customView.measuredWidth}"
        )
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            Log.d(
                "ViewTestActivity",
                "onWindowFocus custom view size:${customView.measuredHeight}, ${customView.measuredWidth}"
            )
        }
    }
}