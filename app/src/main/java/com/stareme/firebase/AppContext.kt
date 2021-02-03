package com.stareme.firebase

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.stareme.PowerUtils

/**
 * Created by wangbin on 18/3/20.
 */
class AppContext: MultiDexApplication() {

    companion object {
        var application: Application? = null

        fun getContext(): Application? {
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        PowerUtils.listenPower()
    }


}