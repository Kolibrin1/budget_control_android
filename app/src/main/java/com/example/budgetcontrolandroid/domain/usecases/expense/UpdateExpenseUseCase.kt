package com.example.budgetcontrolandroid.domain.usecases.expense

import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import javax.inject.Inject

class UpdateExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: ExpenseDto): ExpenseDto {
        return repository.updateExpense(expense)
    }
}