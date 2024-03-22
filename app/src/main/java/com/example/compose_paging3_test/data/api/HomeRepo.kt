package com.cxc.cxctoday.data.repo.home

import com.example.compose_paging3_test.data.ApiResult
import com.example.compose_paging3_test.data.api.HomeApi
import com.example.compose_paging3_test.data.dto.home.HotTagsDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepo(val source: HomeApi) : IHomeRepo {


    override suspend fun getAllTags(
        page: Int,
        perPage: Int,
        keyword: String?
    ): Flow<ApiResult<HotTagsDto>> {
        return flow {
            emit(
                source.getAllTags(
                    page, perPage, keyword
                )
            )
        }
    }


}