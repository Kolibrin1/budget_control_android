package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.TokenResponseDto
import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import kotlinx.serialization.SerialName


class AuthRepositoryImpl(
    private val apiService: ApiService,
) : AuthRepository {
    override suspend fun checkUser(login: String): Boolean {
        return apiService.checkUser(login)
    }

    override suspend fun login(login: String, password: String): TokenResponseDto {
        return apiService.login(LoginRequest(login, password))
    }

    override suspend fun register(
        login: String,
        password: String,
        balance: Double,
    ): TokenResponseDto {
        return apiService.register(RegisterRequest(login, password, balance))
    }
}

data class RegisterRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
    @SerialName("balance") val balance: Double,
    @SerialName("currency") val currency: String = "RUB"
)

data class LoginRequest(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
)