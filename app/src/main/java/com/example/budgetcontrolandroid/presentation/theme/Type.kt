package com.example.budgetcontrolandroid.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.budgetcontrolandroid.R

object AppTypography {
    // Поставщик шрифтов Google Fonts
    private val interFontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val interFontFamily = FontFamily(
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = interFontProvider,
            weight = FontWeight.Normal
        ),
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = interFontProvider,
            weight = FontWeight.Medium
        ),
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = interFontProvider,
            weight = FontWeight.SemiBold
        ),
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = interFontProvider,
            weight = FontWeight.Bold
        ),
        Font(
            googleFont = GoogleFont("Inter"),
            fontProvider = interFontProvider,
            weight = FontWeight.ExtraBold
        )
    )

    private val montserratFontFamily = FontFamily(
        Font(
            googleFont = GoogleFont("Montserrat"),
            fontProvider = interFontProvider,
            weight = FontWeight.Normal
        ),
        Font(
            googleFont = GoogleFont("Montserrat"),
            fontProvider = interFontProvider,
            weight = FontWeight.Medium
        ),
        Font(
            googleFont = GoogleFont("Montserrat"),
            fontProvider = interFontProvider,
            weight = FontWeight.SemiBold
        ),
        Font(
            googleFont = GoogleFont("Montserrat"),
            fontProvider = interFontProvider,
            weight = FontWeight.Bold
        )
    )

    // Функция для создания стиля текста
    private fun createTextStyle(
        fontSize: Float,
        fontWeight: FontWeight,
        color: Color,
        fontStyle: FontStyle? = null,
        fontFamily: String = "Inter"
    ): TextStyle {
        val selectedFontFamily = when (fontFamily) {
            "Inter" -> interFontFamily
            "Montserrat" -> montserratFontFamily
            else -> interFontFamily // По умолчанию Inter
        }

        return TextStyle(
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            color = color,
            fontStyle = fontStyle,
            fontFamily = selectedFontFamily
        )
    }

    // Типографика для тёмной темы
    val textTheme = Typography(
        displayLarge = createTextStyle(
            fontSize = 24f,
            fontWeight = FontWeight.Bold, // w700
            color = AppColors.white
        ),
        displayMedium = createTextStyle(
            fontSize = 20f,
            fontWeight = FontWeight.Bold, // w700
            color = AppColors.white
        ),
        displaySmall = createTextStyle(
            fontSize = 18f,
            fontWeight = FontWeight.SemiBold, // w600
            color = AppColors.white
        ),
        headlineLarge = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = AppColors.white
        ),
        titleLarge = createTextStyle(
            fontSize = 19f,
            fontWeight = FontWeight.Normal,
            color = AppColors.onPrimary
        ),
        titleMedium = createTextStyle(
            fontSize = 16f,
            fontWeight = FontWeight.SemiBold, // w600
            color = AppColors.white
        ),
        titleSmall = createTextStyle(
            fontSize = 16f,
            fontWeight = FontWeight.Normal, // w400
            color = AppColors.white
        ),
        bodyLarge = createTextStyle(
            fontSize = 16f,
            fontWeight = FontWeight.Medium, // w500
            color = AppColors.white,
            fontFamily = "Montserrat"
        ),
        bodyMedium = createTextStyle(
            fontSize = 14f,
            fontWeight = FontWeight.Medium, // w500
            color = AppColors.white
        ),
        bodySmall = createTextStyle(
            fontSize = 13f,
            fontWeight = FontWeight.Normal, // w400
            color = AppColors.white
        ),
        labelLarge = createTextStyle(
            fontSize = 16f,
            fontWeight = FontWeight.ExtraBold, // w800
            color = AppColors.white
        ),
        labelMedium = createTextStyle(
            fontSize = 14f,
            fontWeight = FontWeight.SemiBold, // w600
            color = AppColors.white.copy(alpha = 0.7f)
        ),
        labelSmall = createTextStyle(
            fontSize = 12f,
            fontWeight = FontWeight.Normal, // w400
            color = AppColors.subText
        )
    )

    // Типографика с primary цветом
    val primaryTextTheme = textTheme.copy(
        displayLarge = textTheme.displayLarge.copy(color = AppColors.primary),
        displayMedium = textTheme.displayMedium.copy(color = AppColors.primary),
        displaySmall = textTheme.displaySmall.copy(color = AppColors.primary),
        headlineLarge = textTheme.headlineLarge.copy(color = AppColors.primary),
        titleLarge = textTheme.titleLarge.copy(color = AppColors.primary),
        titleMedium = textTheme.titleMedium.copy(color = AppColors.primary),
        titleSmall = textTheme.titleSmall.copy(color = AppColors.primary),
        bodyLarge = textTheme.bodyLarge.copy(color = AppColors.primary),
        bodyMedium = textTheme.bodyMedium.copy(color = AppColors.primary),
        bodySmall = textTheme.bodySmall.copy(color = AppColors.primary),
        labelLarge = textTheme.labelLarge.copy(color = AppColors.primary),
        labelMedium = textTheme.labelMedium.copy(color = AppColors.primary),
        labelSmall = textTheme.labelSmall.copy(color = AppColors.primary)
    )
}