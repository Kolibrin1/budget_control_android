package com.example.budgetcontrolandroid.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

object AppFunctions {
    fun getIconColor(backgroundColor: Color): Color {
        return if (backgroundColor.luminance() > 0.7) Color.Black else Color.White
    }
}