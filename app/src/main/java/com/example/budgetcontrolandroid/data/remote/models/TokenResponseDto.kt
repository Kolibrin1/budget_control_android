package com.example.budgetcontrolandroid.data.remote.models

import com.google.gson.annotations.SerializedName

data class TokenResponseDto (
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)