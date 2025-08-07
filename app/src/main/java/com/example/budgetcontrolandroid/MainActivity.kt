package com.example.budgetcontrolandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.presentation.theme.BudgetControlAndroidTheme
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthScreen
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.budgetcontrolandroid.presentation.ui.category.CategoryViewModel
import com.example.budgetcontrolandroid.presentation.ui.navigation.RootNavigationScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenRepository: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()

            BudgetControlAndroidTheme {
                AuthFlow(authViewModel, tokenRepository)
            }
        }
    }
}


@Composable
fun AuthFlow(
    authViewModel: AuthViewModel,
    tokenRepository: TokenRepository,
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val accessToken by tokenRepository.getAccessTokenFlow().collectAsState(initial = "")
    val categories by categoryViewModel.categories.collectAsState()
//    val state by authViewModel.state

    // Лог для отладки
//    LaunchedEffect(accessToken) {
//        Log.d("AuthFlow", "Access Token: $accessToken, State: $state")
//    }
    if (accessToken.isNotEmpty()) {
        RootNavigationScreen()
    } else {
        AuthScreen(viewModel = authViewModel)
    }
}