package com.example.compose_paging3_test.data.api

import com.example.compose_paging3_test.data.ApiResult
import com.example.compose_paging3_test.data.dto.home.HotTagsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {


    //1.3.4 全站標籤
    @GET("/book_tag")
    suspend fun getAllTags(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("keyword") keyword: String? = null
    ): ApiResult<HotTagsDto>


}