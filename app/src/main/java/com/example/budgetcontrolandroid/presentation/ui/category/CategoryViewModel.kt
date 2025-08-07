package com.example.budgetcontrolandroid.presentation.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    val categories = categoryRepository.categories

    init {
        viewModelScope.launch {
            tokenRepository.cachedAccessToken.collectLatest { token ->
                if (!token.isNullOrEmpty()) {
                    loadCategories()
                } else {
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                categoryRepository.getAllCategories()  // Синхронизируем репозиторий
            } catch (e: Exception) {
            }
        }
    }

    fun createCategory(name: String, icon: String, color: String) {
        viewModelScope.launch {
            try {
                val token = tokenRepository.cachedAccessToken.value
                    ?: throw IllegalStateException("Токен не доступен")
                categoryRepository.addCategory(name, icon, color)
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val token = tokenRepository.cachedAccessToken.value
                    ?: throw IllegalStateException("Токен не доступен")
                categoryRepository.deleteCategory(categoryId)  // Синхронизируем репозиторий
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }
}