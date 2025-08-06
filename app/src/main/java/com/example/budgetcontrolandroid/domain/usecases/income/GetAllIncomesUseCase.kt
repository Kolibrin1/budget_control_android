package com.example.budgetcontrolandroid.domain.usecases.income

import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import javax.inject.Inject

class GetAllIncomesUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(): List<IncomeDto> {
        return repository.getAllIncomes()
    }
}