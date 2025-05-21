package com.example.jotpackcomposelnstagram.login.data.network

import com.example.jotpackcomposelnstagram.login.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginClient {
    @POST("iniciar-sesion")
    suspend fun doLogin(@Body req: LoginRequest): Response<LoginResponse>
}