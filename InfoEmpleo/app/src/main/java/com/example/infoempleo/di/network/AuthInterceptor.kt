// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/di/network/AuthInterceptor.kt
// InfoEmpleo/app/src/main/java/com/example/infoempleo/di/network/AuthInterceptor.kt
package com.example.infoempleo.di.network

import com.example.infoempleo.di.TokenPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val prefs: TokenPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefs.getJwtToken()
        val req = chain.request().newBuilder()
        if (token != null) {
            req.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(req.build())
    }
}

