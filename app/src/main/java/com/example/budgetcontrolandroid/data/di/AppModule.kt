package com.example.budgetcontrolandroid.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.client.interceptors.AuthInterceptor
import com.example.budgetcontrolandroid.data.remote.client.interceptors.TokenAuthenticator
import com.example.budgetcontrolandroid.data.repositories.AuthRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.ExpenseRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.IncomeRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.ProfileRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import com.example.budgetcontrolandroid.domain.repositories.ProfileRepository
import com.example.budgetcontrolandroid.domain.usecases.auth.RefreshTokenUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun providesTokenRepository(dataStore: DataStore<Preferences>): TokenRepository =
        TokenRepository(dataStore)

    @Provides
    @Singleton
    fun providesAuthInterceptor(
        tokenRepository: TokenRepository,
    ): AuthInterceptor = AuthInterceptor(tokenRepository)

    @Provides
    @Singleton
    fun providesTokenAuthenticator(
        tokenRepository: TokenRepository,
        refreshTokenUseCaseProvider: Provider<RefreshTokenUseCase>
    ): TokenAuthenticator = TokenAuthenticator(tokenRepository, refreshTokenUseCaseProvider)

    @Provides
    @Singleton
    fun providesRefreshTokenUseCase(authRepository: AuthRepository): RefreshTokenUseCase =
        RefreshTokenUseCase(authRepository)

    @Provides
    @Singleton
    fun providesOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providesApiService(okHttpClient: OkHttpClient, gson: Gson): ApiService =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesAuthRepository(apiService: ApiService): AuthRepository =
        AuthRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providesExpenseRepository(apiService: ApiService): ExpenseRepository =
        ExpenseRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providesIncomeRepository(apiService: ApiService): IncomeRepository =
        IncomeRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun providesProfileRepository(apiService: ApiService): ProfileRepository =
        ProfileRepositoryImpl(apiService)
}