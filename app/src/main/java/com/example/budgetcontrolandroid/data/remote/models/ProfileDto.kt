package com.example.budgetcontrolandroid.data.remote.models

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("login")
    val login: String,
)