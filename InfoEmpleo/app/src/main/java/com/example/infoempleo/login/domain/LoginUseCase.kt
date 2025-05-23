package com.example.infoempleo.login.domain

import com.example.infoempleo.login.data.network.LoginRepository
import com.example.infoempleo.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository:LoginRepository) {
    suspend operator fun invoke(correo: String, password: String): LoginResponse? {
        return repository.doLogin(correo, password)
    }
}