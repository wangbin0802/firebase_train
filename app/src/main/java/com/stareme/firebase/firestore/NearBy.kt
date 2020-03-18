package com.stareme.firebase.firestore

import java.io.Serializable

/**
 * Created by wangbin on 18/3/20.
 */
class NearBy : Serializable {
    var ad: Ad? = null
    var alias: String = ""
    override fun toString(): String {
        return "${ad?.flag}, ${ad?.index}, $alias"
    }
}

class Ad : Serializable {
    var flag: Boolean = false
    var index: String = ""
}