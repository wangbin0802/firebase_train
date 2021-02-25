package com.stareme.injection

import java.lang.Exception

class AutoWiredProcess {
    companion object {
        fun bind(obj: Any) {
            val parentClass = obj.javaClass
            val fields = parentClass.declaredFields
            for (field in fields) {
                val autoWireAnnotation = field.getAnnotation(AutoWired::class.java)
                if (autoWireAnnotation != null) {
                    field.isAccessible = true
                    try {
                        val autoCreateClass = field.type
                        val autoCreateConstructor = autoCreateClass.getConstructor()
                        field.set(obj, autoCreateConstructor.newInstance())
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }
}