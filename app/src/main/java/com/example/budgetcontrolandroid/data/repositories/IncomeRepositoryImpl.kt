package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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
        return apiService.deleteIncome(id = incomeId)
    }

}

data class IncomeRequest(
    @SerialName("total_count") val totalCount: Double,
    @SerialName("category_id") val categoryId: Int,
    @SerialName("date")
    @Serializable(with = InstantIso8601Serializer::class)
    val date: Instant,
)