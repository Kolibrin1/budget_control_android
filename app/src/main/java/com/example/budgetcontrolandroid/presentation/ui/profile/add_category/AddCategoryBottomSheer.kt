package com.example.budgetcontrolandroid.presentation.ui.profile.add_category

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.common.AppFunctions.getIconColor
import com.example.budgetcontrolandroid.presentation.components.AppButton
import com.example.budgetcontrolandroid.presentation.components.AppTextField
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.ui.category.CategoryViewModel
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryBottomSheet(
    onDismiss: () -> Unit,
    onCategoryAdded: (String, String, String) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },

        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = AppColors.background
    ) {
        var name by remember { mutableStateOf("") }
        var selectedColor by remember { mutableStateOf(Color.Blue) }
        var selectedIcon by remember { mutableStateOf("icons/categories/icon_1.svg") }
        val icons = List(29) { "icons/categories/icon_${it + 1}.svg" }
        var showColorPicker by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.size(30.dp))
                Text(
                    text = "Создание категории",
                    style = MaterialTheme.typography.displaySmall
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(AppColors.divider)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(selectedColor)
                    .border(2.dp, Color.White, CircleShape)
                    .clickable { showColorPicker = true },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data("file:///android_asset/" + selectedIcon)
                        .decoderFactory(SvgDecoder.Factory())
                        .crossfade(true)
                        .build(),
                    contentDescription = "Selected Icon",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(40.dp),
                    colorFilter = ColorFilter.tint(getIconColor(selectedColor))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it.take(30) },
                hintText = "Введите название",
                hintStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = AppColors.primary.copy(alpha = 0.9f),
                    fontWeight = FontWeight.W400
                ),
                colorBorder = AppColors.primary
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.height(screenHeight.dp * 0.5f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(icons.size) { index ->
                    val icon = icons[index]
                    val isSelected = selectedIcon == icon
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(AppColors.surface)
                            .border(
                                width = 2.dp,
                                color = if (isSelected) AppColors.primary else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedIcon = icon },
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data("file:///android_asset/$icon")
                                .decoderFactory(SvgDecoder.Factory())
                                .crossfade(true)
                                .build(),
                            contentDescription = "Category Icon",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(AppColors.white)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(5.dp)
                                    .size(20.dp),
                                tint = AppColors.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AppButton(
                title = "Сохранить",
                modifier = Modifier.width(200.dp),
                fontWeight = FontWeight.W600,
                fontSize = 16.0,
                radius = 10.0,
                padding = 16.0,
                gradientColors = listOf(
                    AppColors.complementaryBlue,
                    AppColors.primary,
                    AppColors.primary,
                    AppColors.complementaryBlue
                ),
                onClick = {
                    if (name.isNotEmpty()) {
                        viewModel.createCategory(name, selectedIcon, colorToHex(selectedColor))
                        onCategoryAdded(name, colorToHex(selectedColor), selectedIcon)
                        onDismiss()
                    } else {
                        // Показать сообщение об ошибке
                        Toast.makeText(
                            context,
                            "Заполните название!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        if (showColorPicker) {
            AlertDialog(
                onDismissRequest = { showColorPicker = false },
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(AppColors.background),
                containerColor = AppColors.background,
                title = {
                    Text(
                        text = "Выберите цвет",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HarmonyColorPicker(
                            harmonyMode = ColorHarmonyMode.NONE,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(16.dp),
                            color = selectedColor,
                            onColorChanged = { hsvColor ->
                                selectedColor = hsvColor.toColor()
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        AppButton(
                            title = "Сохранить",
                            fontWeight = FontWeight.W600,
                            fontSize = 16.0,
                            radius = 10.0,
                            padding = 16.0,
                            gradientColors = listOf(
                                AppColors.complementaryBlue,
                                AppColors.primary,
                                AppColors.primary,
                                AppColors.complementaryBlue
                            ),
                            onClick = { showColorPicker = false }
                        )
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }
    }
}

private fun colorToHex(color: Color): String {
    return String.format(
        "#%06X",
        (0xFFFFFF and android.graphics.Color.argb(
            (color.alpha * 255).toInt(),
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        ))
    )
}