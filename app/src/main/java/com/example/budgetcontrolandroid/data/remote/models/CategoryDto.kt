package com.example.budgetcontrolandroid.data.remote.models

import com.google.gson.annotations.SerializedName

data class CategoryDto (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("user_id") val userId: Double
)