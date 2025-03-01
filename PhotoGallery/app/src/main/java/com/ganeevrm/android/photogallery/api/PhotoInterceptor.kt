package com.ganeevrm.android.photogallery.api

import android.util.Log
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import com.ganeevrm.android.photogallery.BuildConfig


private const val API_KEY = BuildConfig.API_KEY
class PhotoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl: HttpUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .addQueryParameter("format", "json")
            .addQueryParameter("nojsoncallback", "1")
            .addQueryParameter("extras","url_s")
            .addQueryParameter("safe_search", "1")
            .build()

        Log.d("Tester", "newUrl = " + newUrl)
        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}