package com.example.budgetcontrolandroid.domain.usecases.expense

import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import javax.inject.Inject

class GetAllExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(startDate: String, endDate: String): List<ExpenseDto> {
        return repository.getAllExpenses(startDate, endDate)
    }
}