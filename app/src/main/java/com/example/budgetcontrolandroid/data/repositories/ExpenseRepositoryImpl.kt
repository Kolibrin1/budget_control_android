package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ExpenseRepository {

    override suspend fun getAllExpenses(startDate: String, endDate: String): List<ExpenseDto> {
        return apiService.getAllExpenses(startDate, endDate)
    }

    override suspend fun addExpense(expense: ExpenseDto): ExpenseDto {
        return apiService.addExpense(ExpenseRequest(expense.totalCount, expense.categoryId, expense.date))
    }

    override suspend fun updateExpense(expense: ExpenseDto): ExpenseDto {
        return apiService.updateExpense(id = expense.id, data = ExpenseRequest(expense.totalCount, expense.categoryId, expense.date))
    }

    override suspend fun deleteExpense(expenseId: Int): Boolean {
        return apiService.deleteExpense(id = expenseId)
    }

}

data class ExpenseRequest(
    @SerialName("total_count") val totalCount: Double,
    @SerialName("category_id") val categoryId: Int,
    @SerialName("date")
    @Serializable(with = InstantIso8601Serializer::class)
    val date: Instant,
)


