package io.goooler.demoapp.main.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import io.goooler.demoapp.adapter.rv.paging.BasePagingSource
import io.goooler.demoapp.base.core.BaseViewModel
import io.goooler.demoapp.common.type.CommonConstants
import io.goooler.demoapp.main.model.MainCommonVhModel
import io.goooler.demoapp.main.repository.MainCommonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class MainPagingViewModel : BaseViewModel() {
  private val repository = MainCommonRepository()

  private val removedItemsFlow = MutableStateFlow(emptyList<MainCommonVhModel>())
  private val removedItemsSet = mutableSetOf<MainCommonVhModel>()

  val listData: Flow<PagingData<MainCommonVhModel>> =
    Pager(PagingConfig(CommonConstants.DEFAULT_PAGE_SIZE)) {
      DataSource()
    }.flow
      .cachedIn(viewModelScope)
      .combine(removedItemsFlow) { source, removed ->
        source.filter { it !in removed }
      }

  fun removeItem(vararg item: MainCommonVhModel) {
    removedItemsSet += item
    removedItemsFlow.value = removedItemsSet.toList()
  }

  private inner class DataSource : BasePagingSource<MainCommonVhModel>() {
    override suspend fun fetchListData(page: Int): List<MainCommonVhModel> {
      return repository.getRepoListFromApi("google", page, 30)
        .map { MainCommonVhModel.Repo(it.owner.avatarUrl, it.name, it.fullName) }
    }
  }
}
