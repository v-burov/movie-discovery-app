package com.burov.movie.discovery.app.di

import com.burov.movie.discovery.app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader(ApiHeaders.ACCEPT, ApiHeaders.ACCEPT_TYPE)
            .header(ApiHeaders.AUTHORIZATION, BuildConfig.TMDB_API_KEY)
            .build()
        return chain.proceed(newRequest)
    }
}