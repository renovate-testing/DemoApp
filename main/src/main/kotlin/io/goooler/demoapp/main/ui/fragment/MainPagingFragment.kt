package io.goooler.demoapp.main.ui.fragment

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import io.goooler.demoapp.adapter.rv.paging.BaseRvPagingAdapter
import io.goooler.demoapp.base.util.unsafeLazy
import io.goooler.demoapp.common.base.BaseThemeLazyFragment
import io.goooler.demoapp.common.util.disableRefreshAndLoadMore
import io.goooler.demoapp.common.util.finishRefreshAndLoadMore
import io.goooler.demoapp.common.util.getViewModel
import io.goooler.demoapp.main.databinding.MainPagingFragmentBinding
import io.goooler.demoapp.main.model.MainCommonVhModel
import io.goooler.demoapp.main.ui.adapter.MainPagingRvAdapter
import io.goooler.demoapp.main.vm.MainPagingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainPagingFragment : BaseThemeLazyFragment<MainPagingFragmentBinding>() {

  private val vm: MainPagingViewModel by getViewModel()

  private lateinit var rvAdapter: MainPagingRvAdapter

  private val initData by unsafeLazy {
    lifecycleScope.launch {
      vm.listData.collectLatest(rvAdapter::submitData)
    }
  }

  override fun onFragmentResume() {
    initData
  }

  override fun initOnce() {
    binding.let {
      rvAdapter = MainPagingRvAdapter(listener).apply {
        onLoadStatusListener = listener
      }
      it.rvList.adapter = rvAdapter
      it.smartRefresh.setOnRefreshListener(listener)
      it.listener = listener
    }
  }

  private val listener = object :
    View.OnClickListener,
    BaseRvPagingAdapter.OnLoadStatusListener,
    OnRefreshListener,
    MainPagingRvAdapter.OnEventListener {
    override fun onRefresh(refreshLayout: RefreshLayout) {
      rvAdapter.refresh()
    }

    override fun onClick(v: View) {
      when (v) {
        binding.layoutError.ivError,
        binding.layoutError.tvTip -> {
          rvAdapter.refresh()
          binding.layoutError.root.visibility = View.GONE
        }
        binding.fabRemove -> {
        }
      }
    }

    override fun onNotLoading() {
      binding.smartRefresh.finishRefreshAndLoadMore()
    }

    override fun onNoMoreData() {
      binding.smartRefresh.finishLoadMoreWithNoMoreData()
    }

    override fun onEmpty() {
      binding.smartRefresh.disableRefreshAndLoadMore()
      binding.layoutEmpty.root.visibility = View.VISIBLE
    }

    override fun onError(t: Throwable) {
      binding.smartRefresh.disableRefreshAndLoadMore()
      binding.layoutError.root.visibility = View.VISIBLE
    }

    override fun onItemClick(item: MainCommonVhModel) {
      vm.removeItem(item)
    }
  }
}
