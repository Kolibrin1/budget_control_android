package com.example.budgetcontrolandroid.domain.repositories

import com.example.budgetcontrolandroid.data.remote.models.ProfileDto

interface ProfileRepository {
    suspend fun getCurrentUser() : ProfileDto
}