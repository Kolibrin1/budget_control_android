package com.example.budgetcontrolandroid.presentation.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography

@Composable
fun DiagramIcon(isSelected: Boolean) {
    val iconPath = if (isSelected) "icons/diagram.svg" else "icons/diagram_unselected.svg"
    val title = "Диаграмма"
    NavigationIcon(iconPath = iconPath, isSelected = isSelected, title = title)
}

@Composable
fun ProfileIcon(isSelected: Boolean) {
    val iconPath = if (isSelected) "icons/profile.svg" else "icons/profile_unselected.svg"
    val title = "Профиль"
    NavigationIcon(iconPath = iconPath, isSelected = isSelected, title = title)
}

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    iconPath: String,
    isSelected: Boolean,
    title: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/$iconPath")
                .decoderFactory(SvgDecoder.Factory())
                .crossfade(true)
                .build(),
            contentDescription = "Diagram Icon",
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(
                if (isSelected) AppColors.secondary else AppColors.primary.copy(
                    alpha = 0.7f
                )
            )
        )
        Text(
            title, style = AppTypography.textTheme.labelSmall.copy(
                color = if (isSelected) AppColors.secondary else AppColors.primary.copy(
                    alpha = 0.7f
                )
            )
        )
    }
}