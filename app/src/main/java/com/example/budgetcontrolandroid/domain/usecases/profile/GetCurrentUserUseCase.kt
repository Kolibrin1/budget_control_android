package com.example.budgetcontrolandroid.domain.usecases.profile

import com.example.budgetcontrolandroid.data.remote.models.ProfileDto
import com.example.budgetcontrolandroid.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): ProfileDto {
        return repository.getCurrentUser()
    }
}