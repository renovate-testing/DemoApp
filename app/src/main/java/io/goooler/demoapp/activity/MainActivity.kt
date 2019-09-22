package io.goooler.demoapp.activity

import android.os.Bundle
import android.view.View

import androidx.databinding.DataBindingUtil

import io.goooler.demoapp.R
import io.goooler.demoapp.async.ClickHandler
import io.goooler.demoapp.base.BaseActivity
import io.goooler.demoapp.base.BaseApplication
import io.goooler.demoapp.databinding.ActivityMainBinding
import io.goooler.demoapp.util.ToastUtil

class MainActivity : BaseActivity(), ClickHandler {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.clickHandler = this
    }

    override fun onDestroy() {
        BaseApplication.destroyGlobalObject()
        super.onDestroy()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.buttonPanel) {
            ToastUtil.showToast("button clicked")
        }
    }
}