package com.example.budgetcontrolandroid.presentation.ui.profile.add_expense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import com.example.budgetcontrolandroid.domain.usecases.expense.AddExpenseUseCase
import com.example.budgetcontrolandroid.domain.usecases.expense.UpdateExpenseUseCase
import com.example.budgetcontrolandroid.presentation.ui.diagram.DiagramViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
) : ViewModel() {

    val categories: StateFlow<List<CategoryDto>> = categoryRepository.categories

    private var expense: ExpenseDto? = null

    var error by mutableStateOf("")
        private set

    var totalCount by mutableStateOf("")
        private set
    var selectedDate = mutableStateOf<Instant?>(null)
        private set
    var selectedCategory = mutableStateOf<CategoryDto?>(null)
        private set

    fun onTotalCountChanged(value: String) {
        error = ""
        totalCount = value
    }

    fun onDateSelected(date: Instant) {
        error = ""
        selectedDate.value = date
    }

    fun onCategorySelected(category: CategoryDto) {
        error = ""
        selectedCategory.value = category
    }

    private fun validateFields(): String {
        return when {
            totalCount.isEmpty() || totalCount.toDoubleOrNull() == null || totalCount.toDouble() < 0 -> "Некорректная сумма"
            selectedDate.value == null -> "Дата не выбрана"
            selectedCategory.value == null -> "Категория не выбрана"
            else -> ""
        }
    }

    fun saveExpense(onSave: () -> Unit) {
        if (validateFields() == "") {
            val amount = totalCount.toDoubleOrNull() ?: 0.0
            val newExpense = ExpenseDto(
                id = expense?.id ?: 0,
                totalCount = amount,
                date = selectedDate.value!!,
                categoryId = selectedCategory.value!!.id,
                userId = 1,
                category = selectedCategory.value,
            )
            viewModelScope.launch {
                try {
                    if (expense == null) {
                        addExpenseUseCase(newExpense)
                    } else {
                        updateExpenseUseCase(newExpense)
                    }
                    onSave()
                } catch (e: Exception) {
                    error = e.message ?: "Ошибка сети"
                }
            }
        } else {
            error = validateFields()
        }
    }

    fun loadExpense(diagramViewModel: DiagramViewModel) {
        val expense = diagramViewModel.getEditingExpense()
        expense.let {
            this.expense = expense
            val amount = it.totalCount.toString()
            totalCount = amount
            selectedDate.value = it.date
            selectedCategory.value = it.category
        }
    }
}