package com.stareme.livedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LiveDataViewModel(private val dataSource: DataSource) : ViewModel() {
    val currentTime = dataSource.getCurrentTime()
}

object LiveDataFactory : ViewModelProvider.Factory {
    private val dataSource = DefaultDataSource()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LiveDataViewModel(dataSource) as T
    }

}