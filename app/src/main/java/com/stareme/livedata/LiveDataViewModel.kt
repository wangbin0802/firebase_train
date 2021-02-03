package com.stareme.livedata

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class LiveDataViewModel(private val dataSource: DataSource) : ViewModel() {
    val currentTime = dataSource.getCurrentTime()

    val cacheData = dataSource.cachedValue

    fun onRefresh(view: View) {
        Log.d("LiveDataViewModel", "view:$view")
        viewModelScope.launch {
            dataSource.fetchNewData()
        }
    }

    val stockLiveData = dataSource.stockLiveData.map {
        "$it"
    }
}

object LiveDataFactory : ViewModelProvider.Factory {
    private val dataSource = DefaultDataSource()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LiveDataViewModel(dataSource) as T
    }
}