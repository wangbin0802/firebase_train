package com.stareme.livedata

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LiveDataViewModel(private val dataSource: DataSource) : ViewModel() {
    val currentTime = dataSource.getCurrentTime()

    val cacheData = dataSource.cachedValue

    fun onRefresh() {
        viewModelScope.launch {
            dataSource.fetchNewData()
        }
    }

    val stockLiveData = dataSource.stockLiveData

    fun transform() {
        Transformations.map(stockLiveData) {

        }
    }
}

object LiveDataFactory : ViewModelProvider.Factory {
    private val dataSource = DefaultDataSource()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LiveDataViewModel(dataSource) as T
    }

}