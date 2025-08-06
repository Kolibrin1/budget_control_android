package com.example.budgetcontrolandroid.domain.usecases.auth

import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(refreshToken: String) = authRepository.refresh("Bearer $refreshToken")
}