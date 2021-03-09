package com.jerrypeng31.taipeizoodemoapp.retrofit_utils

import okhttp3.*
import java.io.IOException

class MockAPIResponse{
    lateinit var responseString: String
    var status: Int = 0
}

class MockInterceptor : Interceptor {
    interface MockInterceptorListener {
        fun setAPIResponse(url:String): MockAPIResponse?
    }

    var listener: MockInterceptorListener? = null

    fun setInterceptorListener(listener: MockInterceptorListener){
        this.listener = listener
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val uri = chain.request().url().uri().toString()
        val response = listener?.setAPIResponse(uri)

        if (response?.responseString != null) {

            return chain.proceed(chain.request())
                .newBuilder()
                .code(response.status)
                .message(response.responseString)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        response.responseString.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            val original = chain.request()
            val request = original.newBuilder()
                .method(original.method(), original.body())
                .build()

            return chain.proceed(request)
        }

    }
}

