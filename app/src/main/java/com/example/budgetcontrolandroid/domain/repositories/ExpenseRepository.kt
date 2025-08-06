package com.example.budgetcontrolandroid.domain.repositories

import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto

interface ExpenseRepository {

    suspend fun getAllExpenses(startDate: String, endDate: String) : List<ExpenseDto>

    suspend fun addExpense(expense: ExpenseDto) : ExpenseDto

    suspend fun updateExpense(expense: ExpenseDto) : ExpenseDto

    suspend fun deleteExpense(expenseId: Int) : Boolean
}