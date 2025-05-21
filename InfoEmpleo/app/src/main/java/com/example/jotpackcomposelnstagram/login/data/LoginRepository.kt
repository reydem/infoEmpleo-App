package com.example.jotpackcomposelnstagram.login.data.network

import com.example.jotpackcomposelnstagram.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val api:LoginService) {
    suspend fun doLogin(correo: String, password: String): LoginResponse? {
        return api.doLogin(correo, password)
    }
}