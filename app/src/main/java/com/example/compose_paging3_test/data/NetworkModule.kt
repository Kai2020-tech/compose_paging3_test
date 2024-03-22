package com.example.compose_paging3_test.data

import com.example.compose_paging3_test.BuildConfig
import com.example.compose_paging3_test.data.api.HomeApi
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().retryOnConnectionFailure(true)
        builder.addNetworkInterceptor(HeaderInterceptor())
            //todo,在"我的"載入時,有時會遇到java.net.SocketTimeoutException: timeout報錯
            //出現在HeaderInterceptor的return chain.proceed(builder.build())
            //尚不清楚問題點,先加以下三行測試
            .connectTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BASIC)
//            logging.level = (HttpLoggingInterceptor.Level.BODY)
            builder.addNetworkInterceptor(logging)
        }
        return builder.build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideHomeApi(retrofit: Retrofit): HomeApi = retrofit.create(HomeApi::class.java)
}