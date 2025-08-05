package com.example.budgetcontrolandroid.presentation.ui.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.domain.usecases.GetCheckUserUseCase
import com.example.budgetcontrolandroid.domain.usecases.LoginUseCase
import com.example.budgetcontrolandroid.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val savedStateHandle: SavedStateHandle,
    private val getCheckUserUseCase: GetCheckUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state: MutableState<AuthState> = mutableStateOf(
        savedStateHandle.get<String>("authState")?.let { restoreState(it) } ?: AuthState.Loading
    )

    val state: State<AuthState> = _state

    var login by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var balance by mutableStateOf("0")
        private set

    var error by mutableStateOf(AuthError())

    fun setLogin() : (String) -> Unit = { str ->
        login = str
        error = AuthError()
    }

    fun setPassword() : (String) -> Unit = { str ->
        password = str
        error = AuthError()
    }

    fun setConfirmPassword() : (String) -> Unit = { str ->
        confirmPassword = str
        error = AuthError()
    }

    fun setBalance() : (String) -> Unit = { str ->
        balance = str
        error = AuthError()
    }


    private val _passObscured: MutableState<Boolean> = mutableStateOf(true)
    private val _confirmPassObscured: MutableState<Boolean> = mutableStateOf(true)

    val passObscured get() = _passObscured
    val confirmPassObscured get() = _confirmPassObscured

    fun passObscure() {
        _passObscured.value = !_passObscured.value
    }

    fun confirmPassObscure() {
        _confirmPassObscured.value = !_confirmPassObscured.value
    }

    init {
        viewModelScope.launch {
            val savedToken: String = tokenRepository.getAccessToken()
            if (savedToken.isNotEmpty()) {
                _state.value = AuthState.AuthSuccess
            } else {
                _state.value = AuthState.Initial
            }
            saveState()
        }
    }

    fun setEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.Initial -> {
                event.goBack()
                _state.value = AuthState.Initial
                saveState()
                clearStates()
            }

            is AuthEvent.GoLoginRegister -> {
                _state.value =
                    if (event.type == AuthType.Login) AuthState.Login
                    else AuthState.Register
                saveState()
            }

            is AuthEvent.Login -> {
                viewModelScope.launch {
                    try {
                        val response = loginUseCase(login, password)
                        tokenRepository.saveTokens(response.accessToken, response.refreshToken)
                        _state.value = AuthState.AuthSuccess
                        saveState()
                        clearStates()
                    } catch (e: Exception) {
                        error = AuthError(e.message ?: "Неизвестная ошибка", ErrorType.Login)
                    }
                }
            }

            is AuthEvent.Register -> {
                if (password != confirmPassword) {
                    error = AuthError("Пароли не совпадают", ErrorType.Password)
                    return
                }
                _state.value = AuthState.Loading
                viewModelScope.launch {
                    val check = getCheckUserUseCase(login)
                    if (!check) {
                        try {
                            val response =
                                registerUseCase(login, password, balance.toDouble())
                            tokenRepository.saveTokens(response.accessToken, response.refreshToken)
                            _state.value = AuthState.AuthSuccess
                            saveState()
                            clearStates()
                        } catch (e: Exception) {
                            _state.value = AuthState.Register
                            error = AuthError(e.message ?: "Неизвестная ошибка", ErrorType.Login)
                        }
                    } else {
                        _state.value = AuthState.Register
                        error = AuthError("Данный логин занят", ErrorType.Login)
                    }
                }
            }
        }
    }

    private fun saveState() {
        val stateKey = when (_state.value) {
            is AuthState.Loading -> "LOADING"
            is AuthState.Initial -> "INITIAL"
            is AuthState.Login -> "LOGIN"
            is AuthState.Register -> "REGISTER"
            is AuthState.AuthSuccess -> "AUTH_SUCCESS"
        }
        savedStateHandle["authState"] = stateKey
    }

    private fun restoreState(stateKey: String): AuthState {
        return when (stateKey) {
            "LOADING" -> AuthState.Loading
            "INITIAL" -> AuthState.Initial
            "LOGIN" -> AuthState.Login
            "REGISTER" -> AuthState.Register
            "AUTH_SUCCESS" -> AuthState.AuthSuccess


            else -> AuthState.Loading
        }
    }

    private fun clearStates() {
        login = ""
        password = ""
        confirmPassword = ""
        balance = "0"
        error = AuthError()
    }
}


sealed class AuthEvent {
    data class Initial(val goBack: () -> Unit) : AuthEvent()
    data class GoLoginRegister(val type: AuthType) : AuthEvent()
    data object Login : AuthEvent()
    data object Register : AuthEvent()
}

enum class AuthType {
    Login, Register
}

data class AuthError(val message: String? = null, val type: ErrorType? = null)

enum class ErrorType {
    Login, Password
}

sealed class AuthState {
    data object Loading : AuthState()
    data object Initial : AuthState()
    data object Login : AuthState()

    data object Register : AuthState()

    data object AuthSuccess : AuthState()
}