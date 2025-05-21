package com.example.jotpackcomposelnstagram.login.domain

import com.example.jotpackcomposelnstagram.login.data.network.LoginRepository
import com.example.jotpackcomposelnstagram.login.data.network.response.LoginResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository:LoginRepository) {
    suspend operator fun invoke(correo: String, password: String): LoginResponse? {
        return repository.doLogin(correo, password)
    }
}