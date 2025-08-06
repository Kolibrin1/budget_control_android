package com.example.budgetcontrolandroid.domain.usecases.income

import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import javax.inject.Inject

class DeleteIncomeUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.deleteIncome(id)
    }
}