package com.example.budgetcontrolandroid.data.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

data class IncomeDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: Instant,
    @SerializedName("total_count")
    val totalCount: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("user_id")
    val userId: Int,
    val category: CategoryDto?
)