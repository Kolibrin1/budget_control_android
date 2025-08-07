package com.example.budgetcontrolandroid.domain.usecases.category

import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import javax.inject.Inject

//class AddCategoryUseCase @Inject constructor(
//    private val repository: CategoryRepository
//) {
//    suspend operator fun invoke(name: String, icon: String, color: String): CategoryDto {
//        return repository.addCategory(name, icon, color)
//    }
//}