package com.example.budgetcontrolandroid.presentation.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.budgetcontrolandroid.presentation.theme.AppColors
import com.example.budgetcontrolandroid.presentation.ui.diagram.DiagramScreen
import com.example.budgetcontrolandroid.presentation.ui.profile.ProfileScreen
import com.example.budgetcontrolandroid.presentation.ui.profile.add_expense.AddExpenseScreen
import com.example.budgetcontrolandroid.presentation.ui.profile.add_income.AddIncomeScreen

@Composable
fun RootNavigationScreen() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.background(AppColors.background),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(Color.Transparent)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        ambientColor = AppColors.primary.copy(alpha = 0.15f),
                        spotColor = AppColors.primary.copy(alpha = 0.15f)
                    )
                    .height(80.dp),
                containerColor = AppColors.onBackground.copy(alpha = 0.94f),
                contentColor = Color.Transparent
            ) {
                NavigationBarItem(
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate("diagram") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = { DiagramIcon(isSelected = selectedIndex == 0) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = { ProfileIcon(isSelected = selectedIndex == 1) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        },
        content = { innerPadding ->
            NavHost(navController = navController, startDestination = "diagram") {
                composable("diagram") {
                    DiagramScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(AppColors.background),
                        navController = navController
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        modifier = Modifier.padding(innerPadding), navController = navController
                    )
                }
                composable(
                    "add_expense?isEdit={isEdit}",
                    arguments = listOf(navArgument("isEdit") { defaultValue = false })
                ) { backStackEntry ->

                    val isEdit = backStackEntry.arguments?.getBoolean("isEdit") ?: false
                    AddExpenseScreen(
                        navController = navController,
                        isEdit = isEdit,
                    )
                }
                composable(
                    "add_income?isEdit={isEdit}",
                    arguments = listOf(navArgument("isEdit") { defaultValue = false })
                ) { backStackEntry ->

                    val isEdit = backStackEntry.arguments?.getBoolean("isEdit") ?: false
                    AddIncomeScreen(
                        navController = navController,
                        isEdit = isEdit,
                    )
                }
            }
        }
    )
}