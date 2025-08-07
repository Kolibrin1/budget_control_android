package com.example.budgetcontrolandroid.data.remote.models

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

data class ExpenseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    @Serializable(with = InstantIso8601Serializer::class)
    val date: Instant,
    @SerializedName("total_count")
    val totalCount: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("user_id")
    val userId: Int,
    val category: CategoryDto?
)
