package com.example.budgetcontrolandroid.domain.repositories

import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import kotlinx.coroutines.flow.StateFlow

interface CategoryRepository {

    val categories: StateFlow<List<CategoryDto>>

    suspend fun getAllCategories()

    suspend fun addCategory(name: String, icon: String, color: String) : CategoryDto

    suspend fun deleteCategory(categoryId: Int)
}