package com.example.budgetcontrolandroid.presentation.ui.auth.viewmodel

sealed class AuthEvent {
    data object Initial : AuthEvent()
    data class GoLoginRegister(val type: AuthType) : AuthEvent()
    data object Login : AuthEvent()
    data object Register : AuthEvent()
}

enum class AuthType {
    Login, Register
}