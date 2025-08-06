package com.example.budgetcontrolandroid.domain.usecases.expense

import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(id: Int): Boolean {
        return repository.deleteExpense(id)
    }
}