package com.example.budgetcontrolandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.presentation.theme.BudgetControlAndroidTheme
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthScreen
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthState
import com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel.AuthViewModel
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthInitialView
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthLoginRegView
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var tokenRepository: TokenRepository

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
fun AuthFlow(authViewModel: AuthViewModel, tokenRepository: TokenRepository) {
    val accessToken by tokenRepository.getAccessTokenFlow().collectAsState(initial = "")
//    val state by authViewModel.state

    // Лог для отладки
//    LaunchedEffect(accessToken) {
//        Log.d("AuthFlow", "Access Token: $accessToken, State: $state")
//    }
    if (accessToken.isNotEmpty()) {
        Column {  }
//        MainScreen(authViewModel)
    } else {
        AuthScreen(viewModel = authViewModel)
    }
}