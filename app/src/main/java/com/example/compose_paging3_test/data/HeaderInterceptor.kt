package com.example.compose_paging3_test.data

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Accept", "application/json")
        builder.addHeader("device", "android")
        //TODO: set real language
        builder.addHeader("lang", "zh")
        //TODO: set real uuid
        builder.addHeader("uuid", "111222333444")
//        if (!request.url.encodedPath.contains("fs/") && request.header("No-Auth") == null) {  //粉絲牆圖片下載不要帶token,容易401
//            AppConfig.accessToken?.let { builder.addHeader("Authorization", "Bearer $it") }
//        }

        return chain.proceed(builder.build())
    }
}