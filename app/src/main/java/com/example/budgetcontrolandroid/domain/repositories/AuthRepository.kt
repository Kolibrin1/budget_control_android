package com.example.budgetcontrolandroid.domain.repositories

import com.example.budgetcontrolandroid.data.remote.models.TokenResponseDto

interface AuthRepository {
    suspend fun checkUser(login: String) : Boolean

    suspend fun login(login: String, password: String) : TokenResponseDto

    suspend fun register(login: String, password: String, balance: Double) : TokenResponseDto

    suspend fun refresh(refreshToken: String) : TokenResponseDto
}