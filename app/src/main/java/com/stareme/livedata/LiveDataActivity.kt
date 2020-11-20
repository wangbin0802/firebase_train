package com.stareme.livedata

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import com.stareme.firebase.R
import com.stareme.firebase.databinding.ActivityLiveDataBinding

class LiveDataActivity : AppCompatActivity() {
    private val viewmodel: LiveDataViewModel by viewModels {
        LiveDataFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView<ActivityLiveDataBinding>(this, R.layout.activity_live_data)
        binding.lifecycleOwner = this
        binding.viewmodel = viewmodel

        viewmodel.stockLiveData.observe(this, Observer {
            Log.d("LiveDataActivity", "observe stock data:$it")
        })
    }
}