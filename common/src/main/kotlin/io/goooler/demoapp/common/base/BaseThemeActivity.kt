package io.goooler.demoapp.common.base

import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import io.goooler.demoapp.base.core.BaseActivity

abstract class BaseThemeActivity<VB : ViewDataBinding>
@ContentView constructor(@LayoutRes private val layoutId: Int) :
  BaseActivity(),
  ITheme {

  protected lateinit var binding: VB

  override fun getResources(): Resources {
    return if (ScreenUtils.isPortrait())
      AdaptScreenUtils.adaptWidth(super.getResources(), 360)
    else
      AdaptScreenUtils.adaptHeight(super.getResources(), 640)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView<VB>(this, layoutId).also {
      it.lifecycleOwner = this
    }
    BarUtils.transparentStatusBar(this)
  }

  override fun showLoading() {}

  override fun hideLoading() {}
}
