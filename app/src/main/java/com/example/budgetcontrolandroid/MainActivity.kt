package com.example.budgetcontrolandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.budgetcontrolandroid.presentation.theme.BudgetControlAndroidTheme
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthState
import com.example.budgetcontrolandroid.presentation.ui.auth.AuthViewModel
import com.example.budgetcontrolandroid.presentation.ui.auth.components.AuthLoginRegView
import com.example.budgetcontrolandroid.presentation.ui.auth.components.DisplayGifFromDrawable
import com.example.budgetcontrolandroid.presentation.ui.auth.navigation.AuthNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()

            BudgetControlAndroidTheme {
                AuthFlow(authViewModel)
            }
        }
    }
}


@Composable
fun AuthFlow(authViewModel: AuthViewModel)
{
    when (val state = authViewModel.state.value) {
        is AuthState.AuthSuccess -> Column {  }

//            MainScreen(authViewModel)
        else -> AuthNavigation(authViewModel)
    }
}