package com.jerrypeng31.taipeizoodemoapp.retrofit_utils

import com.jerrypeng31.taipeizoodemoapp.retrofit.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MockRetrofit(interceptor: Interceptor) {
    var apiService: ApiService? = null

    init {
        val client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(interceptor)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .build()
        val instance: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://www.google.com.tw")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = instance.create(ApiService::class.java)
    }
}