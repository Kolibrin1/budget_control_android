package com.example.budgetcontrolandroid.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


private val appColorScheme: ColorScheme = darkColorScheme(
    primary = AppColors.primary,
    onPrimary = AppColors.onPrimary,
    secondary = AppColors.secondary,
    onSecondary = AppColors.onSecondary,
    error = AppColors.error,
    onError = AppColors.onError,
    background = AppColors.background,
    onBackground = AppColors.onBackground,
    surface = AppColors.surface,
    onSurface = AppColors.onSurface,
    outline = AppColors.outline,
    tertiary = AppColors.black,
    outlineVariant = AppColors.white
)

// Светлая тема
val lightColorTheme: ColorScheme
    get() = appColorScheme

// Тёмная тема
val darkColorTheme: ColorScheme
    get() = appColorScheme



@Composable
fun BudgetControlAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkColorTheme
        else -> lightColorTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography.textTheme,
        content = content
    )
}
