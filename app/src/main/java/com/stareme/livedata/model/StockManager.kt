package com.stareme.livedata.model

import java.math.BigDecimal

class StockManager(private val symbol: String) {
     fun requestPriceUpdates(listener: (BigDecimal) -> Unit) {
        listener(BigDecimal(100))
    }

    fun removeUpdate(listener: (BigDecimal) -> Unit) {
        listener(BigDecimal(0))
    }
}