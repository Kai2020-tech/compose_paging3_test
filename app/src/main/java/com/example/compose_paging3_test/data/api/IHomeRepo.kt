package com.cxc.cxctoday.data.repo.home

import com.example.compose_paging3_test.data.ApiResult
import com.example.compose_paging3_test.data.dto.home.HotTagsDto
import kotlinx.coroutines.flow.Flow

interface IHomeRepo {
    suspend fun getAllTags(
        page: Int,
        perPage: Int,
        keyword: String?
    ): Flow<ApiResult<HotTagsDto>>
}