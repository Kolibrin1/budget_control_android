package com.example.budgetcontrolandroid.presentation.ui.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto

@Composable
fun CategoriesGrid(
    categories: List<CategoryDto>,
    onCategorySelected: (CategoryDto) -> Unit,
    onDeleteCategory: (CategoryDto) -> Unit,
    selectedCategory: CategoryDto?,
    onAddCategory: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.heightIn(max = 200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            AddCategoryButton(onClick = onAddCategory)
        }
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = selectedCategory?.id == category.id,
                onClick = { onCategorySelected(category) },
                onLongClick = { onDeleteCategory(category) }
            )
        }
    }
}