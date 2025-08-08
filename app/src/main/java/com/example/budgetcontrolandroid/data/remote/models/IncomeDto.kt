package com.example.budgetcontrolandroid.data.remote.models

import com.example.budgetcontrolandroid.domain.models.TransactionDto
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

data class IncomeDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    override val date: Instant,
    @SerializedName("total_count")
    override val totalCount: Double,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("user_id")
    val userId: Int,
    override val category: CategoryDto?
) : TransactionDto()