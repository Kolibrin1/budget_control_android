package com.example.budgetcontrolandroid.domain.usecases.auth

import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String, balance: Double) =
        authRepository.register(login, password, balance)
}