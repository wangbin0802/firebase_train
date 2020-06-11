package com.stareme.storage

import android.os.Build
import android.os.Environment

/**
 * Created by wangbin on 11/6/20.
 */
const val APP_FOLDER_NAME = "Train"

fun getAppPicturePath(): String {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        // full path
        "${Environment.getExternalStorageDirectory().absolutePath}/" +
                "${Environment.DIRECTORY_PICTURES}/$APP_FOLDER_NAME/"
    } else {
        // relative path
        "${Environment.DIRECTORY_PICTURES}/$APP_FOLDER_NAME/"
    }
}