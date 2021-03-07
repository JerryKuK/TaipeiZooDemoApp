package com.jerrypeng31.taipeizoodemoapp.retrofit

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitUtil {
    companion object {
        const val API_URL = "https://data.taipei/"
        private var apiService: ApiService? = null

        fun getInstance() = RetrofitSingletonHolder.instance
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