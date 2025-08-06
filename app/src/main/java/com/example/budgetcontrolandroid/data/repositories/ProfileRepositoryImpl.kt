package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.ProfileDto
import com.example.budgetcontrolandroid.domain.repositories.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {
    override suspend fun getCurrentUser(): ProfileDto {
        return apiService.getCurrentUser()
    }
}