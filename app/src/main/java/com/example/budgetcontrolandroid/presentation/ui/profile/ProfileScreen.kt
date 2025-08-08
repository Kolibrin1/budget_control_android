package com.example.budgetcontrolandroid.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.presentation.ui.auth.components.DisplayGifFromDrawable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.budgetcontrolandroid.common.toFormattedDate
import com.example.budgetcontrolandroid.presentation.ui.profile.components.ConfirmDialog

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(navController.getBackStackEntry("profile"))
) {
    val state by profileViewModel.state.collectAsState()
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(AppColors.background)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Профиль",
                    style = AppTypography.textTheme.headlineLarge
                )
            }
        },
    ) { pad ->
        Column(
            modifier = Modifier
                .background(AppColors.background)
                .padding(pad)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is ProfileState.Loading -> {
                    DisplayGifFromDrawable()
                }

                is ProfileState.ErrorState -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (state as ProfileState.ErrorState).message,
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { profileViewModel.retryLoad() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                is ProfileState.SuccessState -> {
                    val successState = state as ProfileState.SuccessState
                    BalanceSection(
                        profileViewModel = profileViewModel,
                        onAddExpenseClick = {
                            navController.navigate("add_expense")
                        },
                        onAddIncomeClick = {
                            navController.navigate("add_income")
                        }
                    )
                    Text(
                        "Список Доходов",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(successState.incomes.size) { index ->
                            IncomeRow(
                                income = successState.incomes[index],
                                onEdit = {
                                    profileViewModel.setEditingIncome(it)
                                    navController.navigate("add_income?isEdit=true")
                                },
                                onDelete = {
                                    profileViewModel.setDeletingIncome(it)
                                },
                                currency = "RUB"
                            )
                        }
                    }
                }
            }
        }
    }
    val income = profileViewModel.deletingIncome.collectAsState().value
    if (income != null) {
        ConfirmDialog(
            message = "Вы точно хотите удалить доход от '${income.date.toFormattedDate()}'?",
            onConfirm = { profileViewModel.deleteIncome(income.id) },
            item = income,
            onDismiss = {
                profileViewModel.stopDeletingIncome()
            },
            title = "Удаление дохода"
        )
    }
}

@Composable
fun BalanceSection(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    onAddExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        BalanceRow(
            profileViewModel = profileViewModel
        )
        Spacer(modifier = Modifier.height(25.dp))
        ActionRow(
            label = "Добавить расходы",
            onClick = onAddExpenseClick
        )
        Spacer(modifier = Modifier.height(20.dp))
        ActionRow(
            label = "Добавить доходы",
            onClick = onAddIncomeClick
        )
    }
}

@Composable
private fun BalanceRow(
    profileViewModel: ProfileViewModel
) {

    val profile = profileViewModel.profile.collectAsState().value
    val gradientColors = listOf(
        AppColors.primary,
        AppColors.primary,
        AppColors.complementaryBlue,
        AppColors.complementaryBlue,
        AppColors.complementaryBlue
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AppColors.primary.copy(alpha = 0.3f)
            )
            .background(Brush.linearGradient(gradientColors))
            .padding(vertical = 24.dp, horizontal = 20.dp)

    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Баланс",
                    style = AppTypography.textTheme.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = profile.login,
                    style = AppTypography.textTheme.displaySmall,
                    fontWeight = FontWeight.W600,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${profile.balance}",
                    style = AppTypography.textTheme.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = profile.currency,
                    style = AppTypography.textTheme.titleLarge,
                )
            }
        }
    }
}

@Composable
private fun ActionRow(
    label: String,
    onClick: () -> Unit
) {
    val gradientColors = listOf(
        AppColors.primary,
        AppColors.primary,
        AppColors.complementaryBlue,
        AppColors.complementaryBlue,
        AppColors.complementaryBlue
    )

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.AddCircleOutline,
            contentDescription = null,
            tint = AppColors.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = AppTypography.textTheme.titleMedium.copy(
                brush = Brush.linearGradient(
                    gradientColors
                )
            ),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun IncomeRow(
    income: IncomeDto,
    currency: String,
    onEdit: (IncomeDto) -> Unit,
    onDelete: (IncomeDto) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val buttonWidth = screenWidth.dp * 0.2f

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
                onClick = { onEdit(income) },
                backgroundColor = AppColors.complementaryBlue,
                modifier = Modifier.width(buttonWidth),
            )
            ActionButton(
                icon = Icons.Default.Delete,
                label = "Удалить",
                color = Color.White,
                onClick = { onDelete(income) },
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
                        .background(income.category?.color?.toComposeColor() ?: AppColors.primary)
                ) {
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data("file:///android_asset/" + income.category?.icon)
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .build(),
                        colorFilter = ColorFilter.tint(AppColors.white),
                        contentDescription = income.category?.icon,
                        modifier = Modifier
                            .size(30.dp)
                            .align(alignment = Alignment.Center)
                    )

                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = income.category?.name ?: "Неизвестная категория",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "${income.totalCount} $currency",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
            Text(
                text = income.date.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy\nMM.dd")).toString(),
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun String.toComposeColor(): Color {
    return Color(this.toColorInt())
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