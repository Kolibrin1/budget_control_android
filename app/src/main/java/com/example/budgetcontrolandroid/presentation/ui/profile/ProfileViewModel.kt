package com.example.budgetcontrolandroid.presentation.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.data.remote.models.ProfileDto
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import com.example.budgetcontrolandroid.domain.usecases.income.DeleteIncomeUseCase
import com.example.budgetcontrolandroid.domain.usecases.income.GetAllIncomesUseCase
import com.example.budgetcontrolandroid.domain.usecases.profile.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAllIncomesUseCase: GetAllIncomesUseCase,
    private val deleteIncomeUseCase: DeleteIncomeUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _profile = MutableStateFlow<ProfileDto>(ProfileDto(0, "RUB", 30000.0, "Georgiy2"))
    val profile = _profile.asStateFlow()

    val categories: StateFlow<List<CategoryDto>> = categoryRepository.categories

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state = _state.asStateFlow()

    init {
        loadProfile()
        loadIncomes()
    }

    fun deleteIncome(incomeId: Int) {
        viewModelScope.launch {
            try {
                deleteIncomeUseCase(incomeId)
                // Перезагружаем после удаления
                val currentState = _state.value
                if (currentState is ProfileState.SuccessState) {
                    loadIncomes()
                }
            } catch (e: Exception) {
                _state.value = ProfileState.ErrorState(e.message ?: "Ошибка удаления")
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                _profile.value = getCurrentUserUseCase()
            } catch (e: Exception) {
                _state.value = ProfileState.ErrorState(e.message ?: "Ошибка загрузки расходов")
            }
        }
    }

    private fun loadIncomes() {
        _state.value = ProfileState.Loading
        viewModelScope.launch {
            try {
                delay(1000)
                val dtos = getAllIncomesUseCase()
                val incomes =
                    dtos.map { dto ->
                        val category = categories.value.find { it.id == dto.categoryId }
                        dto.copy(
                            category = category?.copy(icon = category.icon.replace("assets/", ""))
                        )
                    }
                _state.value = ProfileState.SuccessState(incomes)
            } catch (e: Exception) {
                _state.value = ProfileState.ErrorState(e.message ?: "Ошибка загрузки расходов")
            }
        }
    }

    fun retryLoad() {
        loadProfile()
        loadIncomes()
    }
}

sealed class ProfileState {
    data class SuccessState(
        val incomes: List<IncomeDto>
    ) : ProfileState()

    data object Loading : ProfileState()
    data class ErrorState(val message: String) : ProfileState()
}