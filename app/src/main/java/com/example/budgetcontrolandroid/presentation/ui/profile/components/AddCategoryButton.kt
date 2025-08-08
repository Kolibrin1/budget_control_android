package com.example.budgetcontrolandroid.presentation.ui.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetcontrolandroid.presentation.theme.AppColors

@Composable
fun AddCategoryButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .size(60.dp, 80.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .border(BorderStroke(2.dp, AppColors.primary), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить категорию",
                    tint = AppColors.primary,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = "Новая",
                color = AppColors.white,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp)
            )
        }
    }
}