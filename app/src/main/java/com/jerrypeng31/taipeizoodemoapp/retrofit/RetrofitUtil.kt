package com.jerrypeng31.taipeizoodemoapp.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitUtil {
    companion object {
        const val API_URL = "https://data.taipei/"
        private var apiService: ApiService? = null

        fun getInstance() = RetrofitSingletonHolder.instance

        @Singleton
        @Provides
        fun getApiService(): ApiService {
            return apiService ?: getInstance().create(ApiService::class.java)
        }
    }

    private class RetrofitSingletonHolder{
        companion object{
            private val interceptor = HttpLoggingInterceptor()
            init {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            private val client = OkHttpClient().newBuilder()
                .addNetworkInterceptor(interceptor)
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .build()
            val instance: Retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }
}