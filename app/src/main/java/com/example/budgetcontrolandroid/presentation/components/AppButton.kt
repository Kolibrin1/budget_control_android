package com.example.budgetcontrolandroid.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography

@Composable
fun AppButton(
    title: String,
    onClick: () -> Unit,
    isDisabled: Boolean = false,
    padding: Double = 2.0,
    gradientColors: List<Color> = listOf(
        AppColors.complementaryBlue,
        AppColors.primary,
        AppColors.primary,
        AppColors.complementaryBlue
    ),
    icon: @Composable (() -> Unit)? = null,
    height: Double? = null,
    radius: Double? = 12.0,
    textColor: Color? = null,
    fontWeight: FontWeight? = null,
    fontSize: Double? = null,
    child: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = if (isDisabled) {
            {}
        } else onClick,
        modifier = modifier
            .padding(padding.dp)
            .fillMaxWidth()
            .height(height?.dp ?: 36.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(radius?.dp ?: 12.dp),
                clip = false
            )
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset.Zero,
                    end = Offset.Infinite,
                    tileMode = TileMode.Clamp,
                ),
                shape = RoundedCornerShape(radius?.dp ?: 12.dp)
            ),
        enabled = !isDisabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(radius?.dp ?: 12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(6.dp))
            }
            if (child == null) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = textColor ?: Color.White,
                        fontSize = (fontSize ?: 16.0).sp,
                        fontWeight = fontWeight ?: FontWeight.SemiBold, // w600
                        fontFamily = AppTypography.interFontFamily,
                        textAlign = TextAlign.Center
                    )
                )
            } else {
                child()
            }
        }
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    AppButton(
        padding = 16.0,
        title = "Войти",
        onClick = {},
        gradientColors = listOf(AppColors.complementaryBlue, AppColors.primary, AppColors.primary, AppColors.complementaryBlue)
    )
}