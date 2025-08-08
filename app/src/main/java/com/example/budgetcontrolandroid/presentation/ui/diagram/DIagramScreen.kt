package com.example.budgetcontrolandroid.presentation.ui.diagram

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.common.toFormattedDate
import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import com.example.budgetcontrolandroid.presentation.ui.auth.components.DisplayGifFromDrawable
import com.example.budgetcontrolandroid.presentation.ui.profile.components.ConfirmDialog
import com.example.budgetcontrolandroid.presentation.ui.profile.toComposeColor
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagramScreen(
    navController: NavController,
    modifier: Modifier,
    diagramViewModel: DiagramViewModel = hiltViewModel(navController.getBackStackEntry("diagram"))
) {
    val state by diagramViewModel.state.collectAsState()
    val selectedType by diagramViewModel.selectedType.collectAsState()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        val datePickerState = rememberDateRangePickerState()
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
                    Text("Выберите период", modifier = Modifier.padding(bottom = 12.dp))
                    DateRangePicker(
                        showModeToggle = false,
                        state = datePickerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.5f)
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
                                val startMillis = datePickerState.selectedStartDateMillis
                                val endMillis = datePickerState.selectedEndDateMillis
                                if (startMillis != null && endMillis != null) {
                                    val start = Instant.fromEpochMilliseconds(startMillis)
                                        .toLocalDateTime(TimeZone.UTC)
                                        .date
                                        .toJavaLocalDate()
                                    val end = Instant.fromEpochMilliseconds(endMillis)
                                        .toLocalDateTime(TimeZone.UTC)
                                        .date
                                        .toJavaLocalDate()
                                    diagramViewModel.onCustomPeriodSelected(start, end)
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
                Spacer(modifier = Modifier.size(42.dp, 42.dp))
                Text(
                    "Диаграмма",
                    style = AppTypography.textTheme.headlineLarge
                )
                IconButton(
                    onClick = {

                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = AppColors.white)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("file:///android_asset/icons/info.svg")
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build(),
                        colorFilter = ColorFilter.tint(AppColors.primary),
                        contentDescription = "file:///android_asset/icons/info.svg",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        },
    ) { pad ->
        Column(
            modifier = Modifier
                .background(AppColors.background)
                .padding(
                    top = 52.dp,
                    start = pad.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr),
                    end = pad.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
                    bottom = 80.dp
                )
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val currentType =
                when (selectedType) {
                    DiagramType.DAY -> 1
                    DiagramType.WEEK -> 2
                    DiagramType.ANY -> 3
                }

            val customPeriod =
                if (state is DiagramState.SuccessState) (state as DiagramState.SuccessState).customPeriod?.let {
                    CustomPeriod(it.dateFrom, it.dateTo)
                } else null

            FilterPanel(
                selectedType = currentType,
                customPeriod = customPeriod,
                onTypeSelected = { typeInt ->
                    val type = when (typeInt) {
                        1 -> DiagramType.DAY
                        2 -> DiagramType.WEEK
                        else -> DiagramType.ANY
                    }
                    if (type == DiagramType.ANY) {
                        showDatePicker = true
                    } else {
                        diagramViewModel.onTypeSelected(type)
                    }
                },
                onCustomPeriodSelected = { showDatePicker = true }
            )
            Text("Список расходов", color = Color.White, fontSize = 18.sp)
            when (state) {
                is DiagramState.Loading -> {
                    DisplayGifFromDrawable()
                }

                is DiagramState.ErrorState -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (state as DiagramState.ErrorState).message,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { diagramViewModel.retryLoad() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                is DiagramState.SuccessState -> {
                    val successState = state as DiagramState.SuccessState
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(successState.expenses.size) { index ->
                            ExpenseRow(
                                expense = successState.expenses[index],
                                onEdit = {
                                    diagramViewModel.setEditingExpense(it)
                                    navController.navigate("add_expense?isEdit=true")
                                },
                                onDelete = {
                                    diagramViewModel.setDeletingExpense(it)
                                },
                                currency = "RUB"
                            )
                        }
                    }
                }
            }
        }
    }
    val expense = diagramViewModel.deletingExpense.collectAsState().value
    if (expense != null) {
        ConfirmDialog(
            message = "Вы точно хотите удалить расход от '${expense.date.toFormattedDate()}'?",
            onConfirm = { diagramViewModel.deleteExpense(expense.id) },
            item = expense,
            onDismiss = {
                diagramViewModel.stopDeletingExpense()
            },
            title = "Удаление расхода"
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ExpenseRow(
    expense: ExpenseDto,
    currency: String,
    onEdit: (ExpenseDto) -> Unit,
    onDelete: (ExpenseDto) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = screenWidth.dp * 0.2f

    // Состояние для свайпа
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val anchors = mapOf(
        0f to 0,
        -(screenWidth * 0.4f) to 1
    )

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(49.dp)
            .shadow(6.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF5F5F5))
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                enabled = true
            )

    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(AppColors.complementaryBlue),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionButton(
                icon = Icons.Default.Edit,
                label = "Изменить",
                color = Color.White,
                onClick = { onEdit(expense) },
                backgroundColor = AppColors.complementaryBlue,
                modifier = Modifier.width(buttonWidth),
            )
            ActionButton(
                icon = Icons.Default.Delete,
                label = "Удалить",
                color = Color.White,
                onClick = { onDelete(expense) },
                backgroundColor = AppColors.primary,
                modifier = Modifier.width(buttonWidth),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = swipeableState.offset.value.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AppColors.onSecondary.copy(alpha = 0.98f))
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(expense.category?.color?.toComposeColor() ?: AppColors.primary)
                ) {
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("file:///android_asset/" + expense.category?.icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build(),
                        colorFilter = ColorFilter.tint(AppColors.white),
                        contentDescription = expense.category?.icon,
                        modifier = Modifier
                            .size(30.dp)
                            .align(alignment = Alignment.Center)
                    )

                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = expense.category?.name ?: "Неизвестная категория",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "${expense.totalCount} $currency",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
            Text(
                text = expense.date.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy\nMM.dd")).toString(),
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionButton(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    color: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(26.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = color
        )
    }
}

data class CustomPeriod(val dateFrom: LocalDate, val dateTo: LocalDate)

@Composable
fun FilterPanel(
    selectedType: Int,
    customPeriod: CustomPeriod?,
    onTypeSelected: (Int) -> Unit,
    onCustomPeriodSelected: () -> Unit
) {
    val options = listOf(
        Triple("День", 1, null),
        Triple("Неделя", 2, null)
    )
    val isCustomSelected = selectedType == 3
    val periodText = customPeriod?.let { "${it.dateFrom} - ${it.dateTo}" } ?: "Период"

    Box(
        modifier = Modifier
            .padding(top = 32.dp)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .shadow(
                16.dp,
                RoundedCornerShape(16.dp),
                ambientColor = AppColors.primary.copy(alpha = 0.3f)
            )
            .shadow(
                24.dp,
                RoundedCornerShape(16.dp),
                ambientColor = AppColors.primary.copy(alpha = 0.2f)
            )
            .shadow(
                36.dp,
                RoundedCornerShape(16.dp),
                ambientColor = AppColors.primary.copy(alpha = 0.1f)
            )
            .padding(2.dp)
            .background(AppColors.surface)
            .padding(horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { (label, type, _) ->
                FilterButton(
                    label = label,
                    isSelected = selectedType == type,
                    onClick = { onTypeSelected(type) }
                )
            }
            FilterButton(
                label = periodText,
                isSelected = isCustomSelected,
                onClick = onCustomPeriodSelected
            )
        }
    }
}

@Composable
fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .shadow(
                4.dp,
                RoundedCornerShape(12.dp),
                ambientColor = AppColors.primary.copy(alpha = 0.3f)
            )
            .background(if (isSelected) AppColors.primary else AppColors.background)
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .border(1.dp, Color.Transparent, RoundedCornerShape(12.dp))
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = AppColors.white,
            modifier = Modifier.padding(4.dp)
        )
    }
}
