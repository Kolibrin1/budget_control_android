package com.example.budgetcontrolandroid.data.remote.client.interceptors

import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.domain.usecases.auth.RefreshTokenUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val refreshTokenUseCaseProvider: Provider<RefreshTokenUseCase>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null

        synchronized(this) {  // Синхронизация: один рефреш за раз, чтобы избежать спама
            val currentAccessToken = tokenRepository.cachedAccessToken.value
            val oldToken = response.request.header("Authorization")?.removePrefix("Bearer ")?.trim()

            // Если токен уже обновлён (другим запросом), просто retry с новым
            if (currentAccessToken != oldToken && currentAccessToken != null) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            val refreshToken = tokenRepository.cachedRefreshToken.value ?: return null  // Нет refresh — logout

            val useCase = refreshTokenUseCaseProvider.get()

            val newTokens = runBlocking {
                useCase(refreshToken)
            }

            runBlocking {
                tokenRepository.saveTokens(newTokens.accessToken, newTokens.refreshToken)
            }

            // Retry с новым токеном
            return response.request.newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .build()
        }
    }
}