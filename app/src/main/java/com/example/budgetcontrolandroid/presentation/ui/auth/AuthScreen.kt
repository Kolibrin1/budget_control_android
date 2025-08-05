package com.example.budgetcontrolandroid.presentation.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthInitialView
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthLoginRegView
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthEvent
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthState
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background),
        topBar = {
            if (viewModel.state.value is AuthState.Initial) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(AppColors.background),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Авторизация", style = AppTypography.textTheme.headlineLarge)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(AppColors.background)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.setEvent(AuthEvent.Initial)
                        },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = AppColors.white)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back Arrow"
                        )
                    }
                    Text(
                        if (viewModel.state.value is AuthState.Login) "Вход" else "Регистрация",
                        style = AppTypography.textTheme.headlineLarge
                    )
                    Spacer(modifier = Modifier.size(42.dp, 42.dp))
                }
            }
        }
    ) { pad ->
        when (viewModel.state.value) {
            is AuthState.Initial -> AuthInitialView(
                modifier = Modifier
                    .padding(pad)
                    .background(AppColors.background)
                    .fillMaxSize(),
                authViewModel = viewModel,
            )

            else -> AuthLoginRegView(
                modifier = Modifier
                    .padding(pad)
                    .background(AppColors.background)
                    .fillMaxSize(),
                authViewModel = viewModel,
            )
        }

    }
}

