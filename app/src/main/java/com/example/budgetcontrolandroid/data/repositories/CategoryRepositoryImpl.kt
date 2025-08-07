package com.example.budgetcontrolandroid.data.repositories

import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val apiService: ApiService,
) : CategoryRepository {

    private val _categories = MutableStateFlow<List<CategoryDto>>(emptyList())
    override val categories: StateFlow<List<CategoryDto>> = _categories.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            tokenRepository.cachedAccessToken.collectLatest { token ->
                if (!token.isNullOrEmpty()) {
                    getAllCategories()  // Загружаем категории при наличии токена
                } else {
                    _categories.value = emptyList()  // Сбрасываем, если токена нет
                }
            }
        }
    }

    override suspend fun getAllCategories() {
        try {
            val categories = apiService.getAllCategories()
            _categories.value = categories
        } catch (e: Exception) {
            _categories.value = emptyList()  // Обработка ошибки
        }
    }

    override suspend fun addCategory(name: String, icon: String, color: String): CategoryDto {
        val token = tokenRepository.cachedAccessToken.value ?: throw IllegalStateException("Токен не доступен")
        // Предполагаю, что ApiService уже использует токен через AuthInterceptor
        val newCategory = apiService.addCategory(CategoryRequest(name, color, icon))
        val updatedCategories = _categories.value.toMutableList().apply { add(newCategory) }
        _categories.value = updatedCategories
        return newCategory
    }

    override suspend fun deleteCategory(categoryId: Int) {
        val token = tokenRepository.cachedAccessToken.value ?: throw IllegalStateException("Токен не доступен")
        apiService.deleteCategory(categoryId)
        val updatedCategories = _categories.value.toMutableList().apply { removeIf { it.id == categoryId } }
        _categories.value = updatedCategories
    }
}

data class CategoryRequest(
    @SerialName("name") val name: String,
    @SerialName("color") val color: String,
    @SerialName("icon") val icon: String,
)