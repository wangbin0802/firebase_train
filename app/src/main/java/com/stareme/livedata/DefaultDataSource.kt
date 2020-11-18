package com.stareme.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.delay

class DefaultDataSource : DataSource {
    override fun getCurrentTime(): LiveData<Long> {
        return liveData {
            while (true) {
                emit(System.currentTimeMillis())
                delay(1000)
            }
        }
    }


}

interface DataSource {
    fun getCurrentTime(): LiveData<Long>
}