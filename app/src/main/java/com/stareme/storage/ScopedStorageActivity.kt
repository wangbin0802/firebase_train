package com.stareme.storage

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_MOVIES
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.stareme.firebase.R
import java.io.File

class ScopedStorageActivity : AppCompatActivity() {
    private lateinit var tvReadExternalStorageTv: TextView
    private lateinit var requestPermissionBtn: Button
    private lateinit var writeToSdcardBtn: Button
    private lateinit var readExternalBtn: Button
    private lateinit var legacyReadExternalFileBtn: Button
    private lateinit var safReadExternalFileBtn: Button
    private lateinit var safWriteExternalFileBtn: Button
    private lateinit var readImg: ImageView

    companion object {
        const val REQUEST_PERMISSION = 0
        const val REQUEST_OPEN_DOCUMENT = 1
        const val REQUEST_CREATE_DOCUMENT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoped_storage)
        initView()
        updatePermissionState()
        // 向外部公有目录写入
        testCreateExternalFile()
        // 向外部私有目录写入
        testCreatePrivateExternalFile()
    }

    private fun initView() {
        tvReadExternalStorageTv = findViewById(R.id.tvReadExternalStorageState)
        requestPermissionBtn = findViewById(R.id.requestPermissionBtn)
        requestPermissionBtn.setOnClickListener {
            if (checkStorePermission(this)) {
                return@setOnClickListener
            }

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), REQUEST_PERMISSION
            )
        }

        writeToSdcardBtn = findViewById(R.id.writeToSdcardBtn)
        writeToSdcardBtn.setOnClickListener {
            // 向外部共享存储创建图片
            testCreatePicturePublic()
        }

        readExternalBtn = findViewById(R.id.readExternalFileBtn)
        readExternalBtn.setOnClickListener {
            // 查找外部存储目录
            readExternalFileByMediaStore(true)
        }

        legacyReadExternalFileBtn = findViewById(R.id.legacyReadExternalFileBtn)
        legacyReadExternalFileBtn.setOnClickListener {
            if (checkStorePermission(this)) {
                // 读取外部公有目录,传统方式
                testReadExternalFile()
            }
        }

        safReadExternalFileBtn = findViewById(R.id.safReadExternalFileBtn)
        safReadExternalFileBtn.setOnClickListener {
            // 通过SAF存储访问框架模式访问
            selectFileUseSAF()
        }

        safWriteExternalFileBtn = findViewById(R.id.safWriteExternalFileBtn)
        safWriteExternalFileBtn.setOnClickListener {
            // 通过SAF存储访问框架模式创建文件
            createFileUseSAF()
        }

        readImg = findViewById(R.id.read_img)
    }

    private fun updatePermissionState() {
        tvReadExternalStorageTv.text = if (checkStorePermission(this)) {
            "是"
        } else {
            "否"
        }
    }

    private fun testCreateExternalFile() {
        val file = File(Environment.getExternalStorageDirectory(), packageName)
        if (!file.exists()) {
            Log.d("ScopedStorageActivity", "create external file state:${file.mkdirs()}")
        }
    }

    private fun testReadExternalFile() {
        val file = File(
            Environment.getExternalStorageDirectory().absolutePath
                    + File.separator + Environment.DIRECTORY_PICTURES + File.separator + APP_FOLDER_NAME,
//            "1591882433780.png"
            "test.txt"
        )
        Log.d("ScopedStorageActivity", "${file.path}")
        Log.d("ScopedStorageActivity", "test read external content:${file.readText()}")
    }

    private fun selectFileUseSAF() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/*"
            // 我们需要使用ContentResolver.openFileDescriptor读取数据
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_OPEN_DOCUMENT)
    }

    private fun createFileUseSAF() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            type = "text/plain"
            addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_TITLE, "${System.currentTimeMillis()}.txt")
        }
        startActivityForResult(intent, REQUEST_CREATE_DOCUMENT)
    }

    private fun testCreatePrivateExternalFile() {
        val externalPrivateFile = getExternalFilesDir(DIRECTORY_MOVIES)
        Log.d("ScopedStorageActivity", "externalPrivateFile:${externalPrivateFile?.absoluteFile}")
        val file = File(externalPrivateFile, "test")
        if (!file.exists()) {
            Log.d("ScopedStorageActivity", "create external private file state:${file.mkdirs()}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {

                }
            }
            updatePermissionState()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_OPEN_DOCUMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data.also { documentUri ->
                        val fileDescriptor =
                            contentResolver.openFileDescriptor(documentUri!!, "r") ?: return
                        Log.d("ScopedStorageActivity", "open documentUri:$documentUri")
                    }
                }
            }
            REQUEST_CREATE_DOCUMENT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data.also { documentUri ->
                        Log.d("ScopedStorageActivity", "create documentUri:$documentUri")
                    }
                }
            }
        }
    }

    private fun readExternalFileByMediaStore(queryAll: Boolean) {
        var pathKey = ""
        var pathValue = ""
        if (!queryAll) {
            pathKey = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                MediaStore.MediaColumns.DATA
            } else {
                MediaStore.MediaColumns.RELATIVE_PATH
            }
            // RELATIVE_PATH会在路径的最后自动添加/
            pathValue = getAppPicturePath()
        }
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            if (pathKey.isEmpty()) {
                null
            } else {
                "$pathKey LIKE ?"
            },
            if (pathValue.isEmpty()) {
                null
            } else {
                arrayOf("%$pathValue%")
            },
            "${MediaStore.MediaColumns.DATE_ADDED} desc"
        )

        cursor?.also {
            while (it.moveToNext()) {
                val id = it.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val displayName =
                    it.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ScopedStorageActivity", "read external uri:$uri, name:$displayName")
                Toast.makeText(this, "$displayName", Toast.LENGTH_LONG).show()
                showImage(uri)
            }
        }
        cursor?.close()
    }

    private fun testCreatePicturePublic() {
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.timg)
        val displayName = "${System.currentTimeMillis()}.png"
        val mimeType = "image/png"
        val compressFormat = Bitmap.CompressFormat.PNG

        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        val path = getAppPicturePath()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path)
        } else {
            val fileDir = File(path)
            if (!fileDir.exists()) {
                fileDir.mkdir()
            }
            contentValues.put(MediaStore.MediaColumns.DATA, path)
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.also {
            val outputStream = contentResolver.openOutputStream(it)
            outputStream?.also { os ->
                bitmap.compress(compressFormat, 100, os)
                os.close()
                Toast.makeText(this, "添加图片成功", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImage(uri: Uri) {
        val openFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        openFileDescriptor?.apply {
            val bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            readImg.setImageBitmap(bitmap)
        }
        openFileDescriptor?.close()
    }
}