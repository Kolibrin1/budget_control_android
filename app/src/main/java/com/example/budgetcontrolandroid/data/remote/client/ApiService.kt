package com.example.budgetcontrolandroid.data.remote.client

import com.example.budgetcontrolandroid.data.remote.models.ExpenseDto
import com.example.budgetcontrolandroid.data.remote.models.IncomeDto
import com.example.budgetcontrolandroid.data.remote.models.ProfileDto
import com.example.budgetcontrolandroid.data.remote.models.TokenResponseDto
import com.example.budgetcontrolandroid.data.repositories.ExpenseRequest
import com.example.budgetcontrolandroid.data.repositories.IncomeRequest
import com.example.budgetcontrolandroid.data.repositories.LoginRequest
import com.example.budgetcontrolandroid.data.repositories.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @POST("/auth/refresh")
    suspend fun refresh(
        @Header("Authorization") token: String,
    ) : TokenResponseDto

    @GET("/expenses/")
    suspend fun getAllExpenses(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ) : List<ExpenseDto>

    @POST("/expenses/")
    suspend fun addExpense(
        @Body data: ExpenseRequest,
    ) : ExpenseDto

    @PUT("/expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: Int,
        @Body data: ExpenseRequest
    ) : ExpenseDto

    @DELETE("/expenses/{id}")
    suspend fun deleteExpense(
        @Path("id") id: Int,
    ) : Boolean

    @GET("/incomes/my")
    suspend fun getAllIncomes() : List<IncomeDto>

    @POST("/incomes/")
    suspend fun addIncome(
        @Body data: IncomeRequest,
    ) : IncomeDto

    @PUT("/incomes/{id}")
    suspend fun updateIncome(
        @Path("id") id: Int,
        @Body data: IncomeRequest
    ) : IncomeDto

    @DELETE("/incomes/{id}")
    suspend fun deleteIncome(
        @Path("id") id: Int,
    ) : Boolean

    @GET("/users/me")
    suspend fun getCurrentUser() : ProfileDto
}