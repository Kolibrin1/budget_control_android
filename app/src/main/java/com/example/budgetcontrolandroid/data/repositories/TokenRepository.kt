package com.example.budgetcontrolandroid.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
        _cachedAccessToken.value = accessToken
        _cachedRefreshToken.value = refreshToken
        Log.d("TokenRepository", "Saved tokens: access=$accessToken, refresh=$refreshToken")
    }

    // Кэш токена в памяти
    private val _cachedAccessToken = MutableStateFlow<String?>(null)
    val cachedAccessToken = _cachedAccessToken.asStateFlow()
    private val _cachedRefreshToken = MutableStateFlow<String?>(null)
    val cachedRefreshToken = _cachedRefreshToken.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            dataStore.data
                .catch { emit(emptyPreferences()) }
                .map { Pair(it[ACCESS_TOKEN_KEY], it[REFRESH_TOKEN_KEY]) }
                .collect { pair ->
                    _cachedAccessToken.value = pair.first
                    _cachedRefreshToken.value = pair.second
                }
        }
    }

    fun getAccessTokenFlow(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    suspend fun getAccessToken(): String {
        return getAccessTokenFlow().first()
    }

    fun getRefreshTokenFlow(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    suspend fun getRefreshToken(): String {
        return getRefreshTokenFlow().first()
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        _cachedAccessToken.value = null
        _cachedRefreshToken.value = null
    }

    fun onDestroy() {
        scope.cancel()
    }
}