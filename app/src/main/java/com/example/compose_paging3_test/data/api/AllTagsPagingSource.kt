package com.cxc.cxctoday.data.repo.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.compose_paging3_test.data.dto.home.HotTagsDto

class AllTagsPagingSource(
    private val perPage: Int,
    private val keyword: String?,
) : PagingSource<Int, HotTagsDto.Data>() {

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun getRefreshKey(state: PagingState<Int, HotTagsDto.Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HotTagsDto.Data> {
        val nextPageNumber = params.key ?: 1
        var nextKey: Int? = null
        val allTags = mutableListOf<HotTagsDto.Data>()
        listener?.getAllTags(
            page = nextPageNumber,
            perPage = perPage,
            keyword,
        ) {
            allTags.addAll(it.data)
            nextKey = if (it.data.isNotEmpty()) nextPageNumber + 1 else null
        }
        return LoadResult.Page(
            data = allTags,
            prevKey = null,
            nextKey = nextKey
        )
    }

    interface Listener {
        suspend fun getAllTags(
            page: Int,
            perPage: Int,
            keyword: String?,
            callback: (HotTagsDto) -> Unit
        )
    }
}