package com.example.infoempleo.login.data.network

import com.example.infoempleo.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginRepository @Inject constructor(private val api:LoginService) {
    suspend fun doLogin(correo: String, password: String): LoginResponse? {
        return api.doLogin(correo, password)
    }
}