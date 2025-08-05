package com.example.budgetcontrolandroid.data.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseDto (
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)