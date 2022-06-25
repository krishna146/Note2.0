package com.example.note.api

import android.content.SharedPreferences
import android.util.Log
import com.example.note.utils.Constants
import com.example.note.utils.Constants.TAG
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(): Interceptor {
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = sharedPreferences.getString(Constants.USER_TOKEN, null)
        request.addHeader("Authorization","Bearer $token")
        return chain.proceed(request.build())
    }
}