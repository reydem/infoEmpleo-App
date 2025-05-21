package com.example.jotpackcomposelnstagram.login.data.network


import com.example.jotpackcomposelnstagram.login.data.network.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import javax.inject.Inject

class LoginService @Inject constructor(private val loginClient: LoginClient) {
    suspend fun doLogin(correo: String, password: String): LoginResponse? {
        val response = loginClient.doLogin(LoginRequest(correo, password))
        return if (response.isSuccessful) response.body() else null
    }
}