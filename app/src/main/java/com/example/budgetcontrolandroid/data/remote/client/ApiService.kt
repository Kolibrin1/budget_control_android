package com.example.budgetcontrolandroid.data.remote.client

import com.example.budgetcontrolandroid.data.remote.models.TokenResponseDto
import com.example.budgetcontrolandroid.data.repositories.LoginRequest
import com.example.budgetcontrolandroid.data.repositories.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/auth/register")
    suspend fun register(
        @Body data: RegisterRequest,
    ) : TokenResponseDto

    @GET("/auth/check-user/{login}")
    suspend fun checkUser(
        @Path("login") login: String
    ) : Boolean

    @POST("/auth/login")
    suspend fun login(
        @Body data: LoginRequest,
    ) : TokenResponseDto
}