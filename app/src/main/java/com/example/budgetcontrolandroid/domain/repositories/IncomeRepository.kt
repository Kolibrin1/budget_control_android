package com.example.budgetcontrolandroid.domain.repositories

import com.example.budgetcontrolandroid.data.remote.models.IncomeDto

interface IncomeRepository {

    suspend fun getAllIncomes() : List<IncomeDto>

    suspend fun addIncome(income: IncomeDto) : IncomeDto

    suspend fun updateIncome(income: IncomeDto) : IncomeDto

    suspend fun deleteIncome(incomeId: Int) : Boolean
}