package com.example.budgetcontrolandroid.data.remote.client.interceptors

import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Добавляем токен, если путь не содержит "/auth/"
        if (!url.contains("/auth/")) {
            val accessToken = tokenRepository.cachedAccessToken.value
            if (accessToken != null) {
                val authorizedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
                return chain.proceed(authorizedRequest)
            }
        }

        return chain.proceed(originalRequest)
    }
}