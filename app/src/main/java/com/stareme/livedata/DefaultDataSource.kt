package com.stareme.livedata

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class DefaultDataSource : DataSource {
    private val _cachedData = MutableLiveData("This is old data")
    override val cachedValue: LiveData<String> = _cachedData

    override val stockLiveData = StockLiveData("stock")


    override fun getCurrentTime(): LiveData<Long> {
        return liveData {
            while (true) {
                emit(System.currentTimeMillis())
                delay(1000)
            }
        }
    }

    override suspend fun fetchNewData() {
        withContext(Dispatchers.Main) {
            _cachedData.value = "Fetching new data..."
            _cachedData.value = simulateNetworkDataFetch()
        }
    }

    private var counter = 0
    private suspend fun simulateNetworkDataFetch(): String = withContext(Dispatchers.IO) {
        delay(3000)
        counter++
        "New data from request#$counter"
    }
}

interface DataSource {
    fun getCurrentTime(): LiveData<Long>

    val cachedValue: LiveData<String>

    val stockLiveData: StockLiveData

    suspend fun fetchNewData()
}