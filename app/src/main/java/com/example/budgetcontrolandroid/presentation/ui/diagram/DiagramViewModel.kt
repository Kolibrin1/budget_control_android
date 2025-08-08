package com.example.budgetcontrolandroid.presentation.ui.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import com.example.budgetcontrolandroid.domain.usecases.expense.DeleteExpenseUseCase
import com.example.budgetcontrolandroid.domain.usecases.expense.GetAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class DiagramViewModel @Inject constructor(
    private val getAllExpensesUseCase: GetAllExpensesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<DiagramState>(DiagramState.Loading)
    val state = _state.asStateFlow()

    val categories: StateFlow<List<CategoryDto>> = categoryRepository.categories

    private val _selectedType = MutableStateFlow<DiagramType>(DiagramType.DAY)
    val selectedType = _selectedType.asStateFlow()

    private val _editingExpense = MutableStateFlow<ExpenseDto?>(null)

    fun setEditingExpense(expense: ExpenseDto) {
        _editingExpense.value = expense
    }

    fun getEditingExpense() : ExpenseDto {
        val currentState = _editingExpense.value
        _editingExpense.value = null
        return currentState!!
    }

    private val _deletingExpense = MutableStateFlow<ExpenseDto?>(null)
    var deletingExpense = _deletingExpense.asStateFlow()

    fun setDeletingExpense(expense: ExpenseDto) {
        _deletingExpense.value = expense
    }

    fun stopDeletingExpense() {
        _deletingExpense.value = null
    }

    init {
        loadExpenses(DiagramType.DAY)
    }

    fun onTypeSelected(type: DiagramType) {
        if (type != DiagramType.ANY) {
            loadExpenses(type)
        }
    }

    fun onCustomPeriodSelected(start: LocalDate, end: LocalDate) {
        val period = CustomPeriod(start, end)
        loadExpenses(DiagramType.ANY, period)
    }

    fun retryLoad() {
        val currentState = _state.value
        if (currentState is DiagramState.SuccessState) {
            loadExpenses(_selectedType.value, currentState.customPeriod)
        } else {
            loadExpenses(DiagramType.DAY)
        }
    }

    fun deleteExpense(expenseId: Int) {
        viewModelScope.launch {
            try {
                deleteExpenseUseCase(expenseId)
                // Перезагружаем после удаления
                val currentState = _state.value
                if (currentState is DiagramState.SuccessState) {
                    loadExpenses(_selectedType.value, currentState.customPeriod)
                }
            } catch (e: Exception) {
                _state.value = DiagramState.ErrorState(e.message ?: "Ошибка удаления")
            }
        }
    }

    private fun loadExpenses(type: DiagramType, customPeriod: CustomPeriod? = null) {
        _state.value = DiagramState.Loading
        viewModelScope.launch {
            try {
                delay(1000)
                val (startDate, endDate) = calculateDates(type, customPeriod)
                val dtos = getAllExpensesUseCase(startDate, endDate)
                val expenses =
                    dtos.map { dto ->
                        val category = categories.value.find { it.id == dto.categoryId }
                        dto.copy(
                            category = category?.copy(icon = category.icon.replace("assets/", ""))
                        )
                    }
                _selectedType.value = type
                _state.value = DiagramState.SuccessState(customPeriod, expenses)
            } catch (e: Exception) {
                _state.value = DiagramState.ErrorState(e.message ?: "Ошибка загрузки расходов")
            }
        }
    }

    private fun calculateDates(
        type: DiagramType,
        customPeriod: CustomPeriod?
    ): Pair<String, String> {
        val now = LocalDate.now()
        return when (type) {
            DiagramType.DAY -> {
                val start = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                Pair(start, start)
            }

            DiagramType.WEEK -> {
                val start =
                    LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                val end = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                Pair(start, end)
            }

            DiagramType.ANY -> {
                if (customPeriod == null) throw IllegalStateException("Период не выбран")
                Pair(customPeriod.dateFrom.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")), customPeriod.dateTo.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            }
        }
    }
}

sealed class DiagramState {
    data class SuccessState(
        val customPeriod: CustomPeriod?,
        val expenses: List<ExpenseDto>
    ) : DiagramState()

    data object Loading : DiagramState()
    data class ErrorState(val message: String) : DiagramState()
}

enum class DiagramType {
    DAY,
    WEEK,
    ANY
}