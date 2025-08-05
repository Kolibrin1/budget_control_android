package com.example.budgetcontrolandroid.presentation.ui.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthViewModel
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthInitialView
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthLoginRegView

@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "initial") {
        composable("initial") {
            AuthInitialView(
                authViewModel = authViewModel,
                goLoginOrReg = {
                    navController.navigate("loginOrReg")
                },
            )
        }
        composable("loginOrReg") {
            AuthLoginRegView(
                authViewModel = authViewModel,
                goBack = {
                    navController.popBackStack()
                })
        }
    }
}