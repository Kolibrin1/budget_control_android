package com.example.budgetcontrolandroid.domain.models

import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import kotlinx.datetime.Instant

abstract class TransactionDto {
    abstract val category: CategoryDto?
    abstract val totalCount: Double
    abstract val date: Instant
}