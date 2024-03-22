package com.cxc.cxctoday.ui.home.recommend.hot_tags

import android.nfc.tech.MifareUltralight
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cxc.cxctoday.data.repo.home.AllTagsPagingSource
import com.cxc.cxctoday.data.repo.home.IHomeRepo
import com.example.compose_paging3_test.base.BaseViewModel
import com.example.compose_paging3_test.data.dto.home.HotTagsDto
import kotlinx.coroutines.flow.Flow

class HomeAllTagsViewModel(
    private val state: SavedStateHandle,
    private val homeRepo: IHomeRepo
) : BaseViewModel(state),
    AllTagsPagingSource.Listener {

    companion object {
        private const val PER_PAGE = 40  //分頁請求每頁幾筆
    }

    fun getAllTagPaging(keyword: String? = null): Flow<PagingData<HotTagsDto.Data>> {
        return Pager(
            config = PagingConfig(
                pageSize = MifareUltralight.PAGE_SIZE,
                prefetchDistance = PER_PAGE / 4
            )
        ) {
            AllTagsPagingSource(
                perPage = PER_PAGE,
                keyword = keyword,
            ).apply {
                setListener(this@HomeAllTagsViewModel)
            }
        }.flow.cachedIn(viewModelScope)
    }

    override suspend fun getAllTags(
        page: Int,
        perPage: Int,
        keyword: String?,
        callback: (HotTagsDto) -> Unit
    ) {
        homeRepo.getAllTags(page, perPage, keyword).setupForData().collect {
            callback.invoke(it)
        }
    }
}