package com.stareme.storage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.stareme.firebase.R

class ScopedStorageActivity : AppCompatActivity() {
    private lateinit var tvReadExternalStorageTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoped_storage)
        updatePermissionState()
    }

    private fun updatePermissionState() {
        tvReadExternalStorageTv.text = if (checkStorePermission(this)) {
            "是"
        } else {
            "否"
        }
    }
}