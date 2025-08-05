package com.example.budgetcontrolandroid.presentation.ui.auth.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.presentation.components.AppButton
import com.example.budgetcontrolandroid.presentation.components.AppTextField
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthEvent
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthState
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthViewModel
import com.example.budgetcontrolandroid.presentation.ui.auth.ErrorType

@Composable
fun AuthLoginRegView(
    authViewModel: AuthViewModel,
    goBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.background),
        topBar = {
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
                        authViewModel.setEvent(AuthEvent.Initial(goBack = goBack))
                    },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = AppColors.white)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back Arrow"
                    )
                }
                Text(
                    if (authViewModel.state.value is AuthState.Login) "Вход" else "Регистрация",
                    style = AppTypography.textTheme.headlineLarge
                )
                Spacer(modifier = Modifier.size(42.dp, 42.dp))
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .background(AppColors.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (authViewModel.state.value is AuthState.Loading) {
                DisplayGifFromDrawable()
            } else {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Логин",
                        style = AppTypography.textTheme.bodyLarge.copy(textAlign = TextAlign.Start),
                        modifier = Modifier.padding(16.dp),
                    )
                    AppTextField(
                        value = authViewModel.login,
                        onValueChange = authViewModel.setLogin(),
                        colorBorder = AppColors.primary,
                        errorText = if (authViewModel.error.type == ErrorType.Login) authViewModel.error.message else null
                    )
                    Text(
                        "Пароль",
                        style = AppTypography.textTheme.bodyLarge.copy(textAlign = TextAlign.Start),
                        modifier = Modifier.padding(16.dp),
                    )
                    AppTextField(
                        value = authViewModel.password,
                        onValueChange = authViewModel.setPassword(),
                        colorBorder = AppColors.primary,
                        obscureText = authViewModel.passObscured.value,
                        suffix = {
                            IconButton(onClick = {
                                authViewModel.passObscure()
                            }) {
                                val context = LocalContext.current

                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(if (authViewModel.passObscured.value) "file:///android_asset/icons/obscured.svg" else "file:///android_asset/icons/non_obscured.svg")
                                        .decoderFactory(SvgDecoder.Factory())
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "My SVG Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        errorText = if (authViewModel.error.type == ErrorType.Password) authViewModel.error.message else null
                    )
                    if (authViewModel.state.value is AuthState.Register) {
                        Text(
                            "Подтвердить пароль",
                            style = AppTypography.textTheme.bodyLarge.copy(textAlign = TextAlign.Start),
                            modifier = Modifier.padding(16.dp),
                        )
                        AppTextField(
                            value = authViewModel.confirmPassword,
                            onValueChange = authViewModel.setConfirmPassword(),
                            colorBorder = AppColors.primary,
                            obscureText = authViewModel.confirmPassObscured.value,
                            suffix = {
                                IconButton(onClick = {
                                    authViewModel.confirmPassObscure()
                                }) {
                                    val context = LocalContext.current
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(if (authViewModel.confirmPassObscured.value) "file:///android_asset/icons/obscured.svg" else "file:///android_asset/icons/non_obscured.svg")
                                            .decoderFactory(SvgDecoder.Factory())
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "My SVG Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            errorText = if (authViewModel.error.type == ErrorType.Password) authViewModel.error.message else null
                        )
                        Text(
                            "Баланс",
                            style = AppTypography.textTheme.bodyLarge.copy(textAlign = TextAlign.Start),
                            modifier = Modifier.padding(16.dp),
                        )
                        AppTextField(
                            value = authViewModel.balance,
                            onValueChange = authViewModel.setBalance(),
                            textInputType = KeyboardType.Number,
                            colorBorder = AppColors.primary,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(0.5f))
                AppButton(
                    fontWeight = FontWeight.W600,
                    radius = 10.0,
                    padding = 16.0,
                    fontSize = 16.0,
                    title = "Продолжить",
                    onClick = {
                        if (authViewModel.state.value is AuthState.Login) {
                            authViewModel.setEvent(AuthEvent.Login)
                        } else {
                            authViewModel.setEvent(AuthEvent.Register)
                        }
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
    }
}


@Composable
fun DisplayGifFromDrawable(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(
                context.resources.getIdentifier(
                    "coin_flip_transparent_unscreen",
                    "drawable",
                    context.packageName
                )
            )
            .decoderFactory(GifDecoder.Factory())
            .crossfade(true)
            .build(),
        contentDescription = "Animated GIF",
        modifier = modifier.size(100.dp)
    )
}

@Preview
@Composable
private fun AuthLoginPreview() {
    AuthLoginRegView(
        authViewModel = hiltViewModel(),
        goBack = {}
    )
}
