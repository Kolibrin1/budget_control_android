package com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel

sealed class AuthState {
    data object Loading : AuthState()
    data object Initial : AuthState()
    data object Login : AuthState()
    data object Register : AuthState()
    data object AuthSuccess : AuthState()
}