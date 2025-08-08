package com.example.budgetcontrolandroid.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.SubcomposeAsyncImage
import com.example.budgetcontrolandroid.common.AppFunctions.getIconColor
import com.example.budgetcontrolandroid.domain.models.TransactionDto
import com.example.budgetcontrolandroid.presentation.components.AppButton
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    item: TransactionDto,
    confirmText: String? = null,
    cancelText: String? = null,
    confirmColors: List<Color> = listOf(
        AppColors.complementaryBlue,
        AppColors.primary,
        AppColors.primary,
        AppColors.complementaryBlue
    ),
    cancelColors: List<Color> = listOf(
        AppColors.surface,
        AppColors.surface,
        AppColors.surface,
        AppColors.surface
    ),
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(AppColors.background),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = AppTypography.textTheme.displayMedium
                )
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    style = AppTypography.textTheme.bodyLarge.copy(
                        color = AppColors.subText
                    )
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color((item.category?.color ?: "").toColorInt()))
                    ) {
                        SubcomposeAsyncImage(
                            model = "file:///android_asset/" + (item.category?.icon ?: ""),
                            contentDescription = "Category Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center),
                            colorFilter = ColorFilter.tint(
                                getIconColor(
                                    Color(
                                        (item.category?.color ?: "").toColorInt()
                                    )
                                )
                            )
                        )
                    }
                    Text(
                        text = item.category?.name ?: "",
                        style = AppTypography.textTheme.bodyLarge.copy(color = AppColors.white)
                    )
                    Text(
                        text = "${item.totalCount} RUB",
                        style = AppTypography.textTheme.bodyMedium.copy(color = AppColors.white)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    AppButton(
                        title = cancelText ?: "Отмена",
                        modifier = Modifier.weight(1f),
                        gradientColors = cancelColors,
                        onClick = { onDismiss() }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AppButton(
                        title = confirmText ?: "Удалить",
                        modifier = Modifier.weight(1f),
                        gradientColors = confirmColors,
                        onClick = {
                            onDismiss()
                            onConfirm()
                        }
                    )
                }
            }
        }
    )
}