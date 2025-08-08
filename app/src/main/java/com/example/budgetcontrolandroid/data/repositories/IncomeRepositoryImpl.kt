package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : IncomeRepository {

    override suspend fun getAllIncomes(): List<IncomeDto> {
        return apiService.getAllIncomes()
    }

    override suspend fun addIncome(income: IncomeDto): IncomeDto {
        return apiService.addIncome(IncomeRequest(income.totalCount, income.categoryId, income.date))
    }

    override suspend fun updateIncome(income: IncomeDto): IncomeDto {
        return apiService.updateIncome(id = income.id, data = IncomeRequest(income.totalCount, income.categoryId, income.date))
    }

    override suspend fun deleteIncome(incomeId: Int): Boolean {
        apiService.deleteIncome(id = incomeId)
        return true
    }

}

data class IncomeRequest(
    @SerializedName("total_count") val totalCount: Double,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("date")
    val date: Instant,
)