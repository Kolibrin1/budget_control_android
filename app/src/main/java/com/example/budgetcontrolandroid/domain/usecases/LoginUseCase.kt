package com.example.budgetcontrolandroid.domain.usecases

import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, password: String) = authRepository.login(login, password)
}