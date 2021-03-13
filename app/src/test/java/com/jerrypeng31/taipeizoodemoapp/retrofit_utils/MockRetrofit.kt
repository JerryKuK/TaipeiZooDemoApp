package com.jerrypeng31.taipeizoodemoapp.retrofit_utils

import com.jerrypeng31.taipeizoodemoapp.retrofit.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MockRetrofit(interceptor: Interceptor) {
    companion object{
        var apiService: ApiService? = null
        lateinit var retrofit: Retrofit
        lateinit var mockWebServer: MockWebServer

        fun init(){
            mockWebServer = MockWebServer()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        fun serverShutdown(){
            mockWebServer.shutdown()
        }
    }
}