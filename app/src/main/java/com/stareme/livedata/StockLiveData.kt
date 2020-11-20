package com.stareme.livedata

import android.util.Log
import androidx.lifecycle.LiveData
import com.stareme.livedata.model.StockManager
import java.math.BigDecimal

class StockLiveData(symbool: String) : LiveData<BigDecimal>() {
    private val stockManager = StockManager(symbool)

    private val listener = {
        price: BigDecimal ->
        value = price
    }

    override fun onActive() {
        super.onActive()
        Log.w("StockLiveData", "onActive.....")
        stockManager.requestPriceUpdates(listener)
    }

    override fun onInactive() {
        super.onInactive()
        Log.w("StockLiveData", "onInactive.....")
        stockManager.removeUpdate(listener)
    }
}