package com.example.budgetcontrolandroid.presentation.ui.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetcontrolandroid.R
import com.example.budgetcontrolandroid.presentation.components.AppButton
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthEvent
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthType
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthViewModel

@Composable
fun AuthInitialView(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(10f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.init),
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Добро пожаловать в ваш личный помощник финансов! Контролируйте свой бюджет и ваши расходы!",
                style = AppTypography.textTheme.titleLarge.copy(textAlign = TextAlign.Center),
                modifier = Modifier.padding(16.dp, top = 48.dp),
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        AppButton(
            fontWeight = FontWeight.W600,
            radius = 10.0,
            padding = 16.0,
            fontSize = 16.0,
            title = "Войти",
            onClick = {
                authViewModel.setEvent(AuthEvent.GoLoginRegister(type = AuthType.Login))
            },
            gradientColors = listOf(
                AppColors.complementaryBlue,
                AppColors.primary,
                AppColors.primary,
                AppColors.complementaryBlue
            )
        )
        AppButton(
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.W600,
            radius = 10.0,
            padding = 16.0,
            fontSize = 16.0,
            title = "Зарегистрироваться",
            onClick = {
                authViewModel.setEvent(AuthEvent.GoLoginRegister(type = AuthType.Register))
            },
            gradientColors = listOf(
                AppColors.complementaryBlue,
                AppColors.primary,
                AppColors.primary,
                AppColors.complementaryBlue
            )
        )
    }
}

@Preview
@Composable
private fun AuthInitialPreview() {
    AuthInitialView(
        authViewModel = hiltViewModel(),
    )
}