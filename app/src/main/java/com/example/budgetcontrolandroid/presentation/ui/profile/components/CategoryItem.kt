package com.example.budgetcontrolandroid.presentation.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.budgetcontrolandroid.common.AppFunctions
import com.example.budgetcontrolandroid.data.remote.models.CategoryDto
import com.example.budgetcontrolandroid.presentation.theme.AppColors

@Composable
fun CategoryItem(
    category: CategoryDto,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp, 80.dp)
            .combinedClickable(onLongClick = onLongClick, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color(category.color.toColorInt()), shape = CircleShape)
                    .width(60.dp)
                    .height(60.dp)
                    .border(
                        2.dp,
                        if (isSelected) AppColors.primary else Color.Transparent,
                        CircleShape
                    )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("file:///android_asset/" + category.icon.replace("assets/", ""))
                        .decoderFactory(SvgDecoder.Factory())
                        .build(),
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(AppFunctions.getIconColor(Color(category.color.toColorInt())))
                )
            }

            Text(
                text = category.name,
                color = if (isSelected) AppColors.primary else AppColors.white,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
        }
    }
}