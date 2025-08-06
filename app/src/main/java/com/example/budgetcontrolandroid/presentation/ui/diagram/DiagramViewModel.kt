package com.example.budgetcontrolandroid.presentation.ui.diagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.domain.usecases.expense.DeleteExpenseUseCase
import com.example.budgetcontrolandroid.domain.usecases.expense.GetAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DiagramViewModel @Inject constructor(
    private val getAllExpensesUseCase: GetAllExpensesUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<DiagramState>(DiagramState.Loading)
    val state = _state.asStateFlow()

    private val _selectedType = MutableStateFlow<DiagramType>(DiagramType.DAY)
    val selectedType = _selectedType.asStateFlow()

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
                _selectedType.value = type
                _state.value = DiagramState.SuccessState(customPeriod, dtos)
            } catch (e: Exception) {
                _state.value = DiagramState.ErrorState(e.message ?: "Ошибка загрузки расходов")
            }
        }
    }

    private fun calculateDates(type: DiagramType, customPeriod: CustomPeriod?): Pair<String, String> {
        val now = LocalDate.now()
        return when (type) {
            DiagramType.DAY -> {
                val start = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                Pair(start, start)
            }
            DiagramType.WEEK -> {
                val start = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                val end = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                Pair(start, end)
            }
            DiagramType.ANY -> {
                if (customPeriod == null) throw IllegalStateException("Период не выбран")
                Pair(customPeriod.dateFrom.toString(), customPeriod.dateTo.toString())
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