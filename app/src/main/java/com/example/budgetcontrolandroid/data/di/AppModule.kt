package com.example.budgetcontrolandroid.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.budgetcontrolandroid.data.remote.client.ApiService
import com.example.budgetcontrolandroid.data.remote.client.interceptors.AuthInterceptor
import com.example.budgetcontrolandroid.data.remote.client.interceptors.TokenAuthenticator
import com.example.budgetcontrolandroid.data.repositories.AuthRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.CategoryRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.ExpenseRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.IncomeRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.ProfileRepositoryImpl
import com.example.budgetcontrolandroid.data.repositories.TokenRepository
import com.example.budgetcontrolandroid.domain.repositories.AuthRepository
import com.example.budgetcontrolandroid.domain.repositories.CategoryRepository
import com.example.budgetcontrolandroid.domain.repositories.ExpenseRepository
import com.example.budgetcontrolandroid.domain.repositories.IncomeRepository
import com.example.budgetcontrolandroid.domain.repositories.ProfileRepository
import com.example.budgetcontrolandroid.domain.usecases.auth.RefreshTokenUseCase
import com.example.budgetcontrolandroid.domain.utils.InstantDateDeserializer
import com.example.budgetcontrolandroid.domain.utils.InstantDateSerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Instant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun providesHttpLoggingInterceptor(
    ): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

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
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().registerTypeAdapter(
        Instant::class.java,
        InstantDateDeserializer()
    ).registerTypeAdapter(
        Instant::class.java,
        InstantDateSerializer()
    )
        .create()

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

    @Provides
    @Singleton
    fun providesCategoryRepository(
        apiService: ApiService,
        tokenRepository: TokenRepository
    ): CategoryRepository =
        CategoryRepositoryImpl(tokenRepository, apiService)
}