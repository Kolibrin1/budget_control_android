package com.example.budgetcontrolandroid.domain.usecases.auth

import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import javax.inject.Inject

class GetCheckUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(login: String) = repository.checkUser(login)
}