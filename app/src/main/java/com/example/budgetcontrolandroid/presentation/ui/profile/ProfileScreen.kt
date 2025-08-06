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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
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
                .padding(pad)
                .fillMaxSize()
                .background(AppColors.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BalanceSection()
        }
    }
}

@Composable
fun BalanceSection(
    modifier: Modifier = Modifier,
    balance: Double = 8708.0,
    currency: String = "RUB",
    username: String = "GeorgiyK",
    onAddExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        BalanceRow(
            balance = balance,
            currency = currency,
            username = username
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
    balance: Double,
    currency: String,
    username: String
) {
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
                ambientColor = Color(0xFF673AB7).copy(alpha = 0.3f)  // AppColors.primary opacity
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
                    text = username,
                    style = AppTypography.textTheme.displaySmall,
                    fontWeight = FontWeight.W600,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$balance",
                    style = AppTypography.textTheme.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = currency,
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
            style = AppTypography.textTheme.titleMedium.copy(brush = Brush.linearGradient(gradientColors)),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}