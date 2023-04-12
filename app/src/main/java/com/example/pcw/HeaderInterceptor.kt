package com.example.pcw

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Headers

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept:","application/json")
            .addHeader("ApiKey","asdasdsad")
            .build()
        return  chain.proceed(request)
    }
}