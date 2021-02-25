package com.stareme.injection

import android.app.Activity
import android.view.View

class ButterKnifeProcess {
    companion object {
        fun bind(activity: Activity) {
            val parentClass = activity.javaClass
            val fields = parentClass.declaredFields
//            val methods = parentClass.declaredMethods
            for (field in fields) {
                val annotation = field.getAnnotation(BindView::class.java)
                if (annotation != null) {
                    field.isAccessible = true
                    val viewId = annotation.value
                    val view = activity.findViewById<View>(viewId)
                    val autoCreateClass = field.type
                    field.set(activity, view)
                }
            }
        }
    }
}