package com.example.budgetcontrolandroid.presentation.ui.profile.add_income

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.budgetcontrolandroid.common.toFormattedDate
import com.example.budgetcontrolandroid.presentation.components.AppButton
import com.example.budgetcontrolandroid.presentation.components.AppTextField
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import com.example.budgetcontrolandroid.presentation.ui.diagram.DiagramViewModel
import com.example.budgetcontrolandroid.presentation.ui.profile.ProfileViewModel
import com.example.budgetcontrolandroid.presentation.ui.profile.add_category.AddCategoryBottomSheet
import com.example.budgetcontrolandroid.presentation.ui.profile.components.CategoriesGrid
import kotlinx.datetime.toKotlinInstant
import java.time.Instant

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    isEdit: Boolean,
    profileViewModel: ProfileViewModel = hiltViewModel(navController.getBackStackEntry("profile"))
) {
    val viewModel: AddIncomeViewModel = hiltViewModel()

    var showDatePicker by remember { mutableStateOf(false) }
    var showAddCategory by remember { mutableStateOf(false) }

    LaunchedEffect(isEdit) {
        if (isEdit) {
            viewModel.loadIncome(profileViewModel = profileViewModel)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(AppColors.background)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = AppColors.white
                    )
                }
                Text(
                    if (isEdit) "Изменить доход" else "Добавить доход",
                    style = AppTypography.textTheme.headlineLarge
                )
                Spacer(modifier = Modifier.size(42.dp, 42.dp))
            }
        }
    ) { padding ->
        val cateogories = viewModel.categories.collectAsState().value
        Column(
            modifier = modifier
                .background(AppColors.background)
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.error,
                style = AppTypography.textTheme.displayMedium.copy(color = AppColors.error),
            )
            AppTextField(
                value = viewModel.totalCount,
                onValueChange = { viewModel.onTotalCountChanged(it) },
                hintText = "Укажите сумму",
                colorBorder = AppColors.primary,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = viewModel.selectedDate.value?.toFormattedDate() ?: "Выберите дату",
                style = AppTypography.textTheme.displaySmall.copy(color = AppColors.primary),
                modifier = Modifier
                    .clickable { showDatePicker = true }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Выберите категорию",
                style = AppTypography.textTheme.displaySmall,
            )
            Spacer(modifier = Modifier.height(10.dp))
            CategoriesGrid(
                onCategorySelected = { viewModel.onCategorySelected(it) },
                selectedCategory = viewModel.selectedCategory.value,
                onAddCategory = { showAddCategory = true },
                onDeleteCategory = {},
                categories = cateogories,
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .weight(1f)
            )
            AppButton(
                modifier = Modifier.padding(bottom = 40.dp),
                fontWeight = FontWeight.W600,
                radius = 10.0,
                padding = 16.0,
                fontSize = 16.0,
                title = if (isEdit) "Изменить" else "Добавить",
                onClick = {
                    viewModel.saveIncome() {
                        profileViewModel.retryLoad()
                        navController.popBackStack()
                    }
                },
            )
        }
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.selectedDate.value?.toEpochMilliseconds()
                    ?: System.currentTimeMillis()
            )
            ModalBottomSheet(
                containerColor = AppColors.background,
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),
                onDismissRequest = { showDatePicker = false },
                content = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                    ) {
                        Text("Выберите дату", modifier = Modifier.padding(bottom = 12.dp))
                        DatePicker(
                            showModeToggle = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeight * 0.5f),
                            state = datePickerState,
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary),
                                onClick = {
                                    val dateMillis = datePickerState.selectedDateMillis
                                    if (dateMillis != null) {
                                        viewModel.onDateSelected(
                                            Instant.ofEpochMilli(dateMillis).toKotlinInstant()
                                        )
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("Подтвердить")
                            }
                            Button(
                                onClick = { showDatePicker = false },
                                colors = ButtonDefaults.buttonColors(containerColor = AppColors.primary),
                            ) {
                                Text("Отмена")
                            }
                        }
                    }
                },
            )
        }

        if (showAddCategory) {
            AddCategoryBottomSheet(
                onDismiss = { showAddCategory = false },
                onCategoryAdded = { name, color, icon ->
                    showAddCategory = false
                }
            )
        }
    }
}









