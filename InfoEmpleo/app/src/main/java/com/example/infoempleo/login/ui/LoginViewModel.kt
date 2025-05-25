// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/ui/LoginViewModel.kt
package com.example.infoempleo.login.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoempleo.di.TokenPreferences
import com.example.infoempleo.login.data.network.response.LoginResponse
import com.example.infoempleo.login.domain.LoginUseCase
import com.example.infoempleo.login.di.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenPrefs: TokenPreferences,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable: LiveData<Boolean> = _isLoginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _isLoginEnable.value = Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length > 6
    }

    fun onLoginSelected(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.i("LoginViewModel", "entered onLoginSelected")
            _isLoading.value = true

            // Ejecutamos el caso de uso
            val loginResp: LoginResponse? =
                loginUseCase(email.value.orEmpty(), password.value.orEmpty())

            // Verificamos el token
            val token = loginResp?.token
            val success = !token.isNullOrEmpty()

            if (success && loginResp != null) {
                // Guardamos el JWT para futuras peticiones
                tokenPrefs.saveJwtToken(token!!)
                // Guardamos rol y correo según el backend
                tokenPrefs.saveEsReclutador(loginResp.esReclutador)
                tokenPrefs.saveCorreo(loginResp.correo.orEmpty())
                // Actualizamos el StateFlow para notificar a la UI
                sessionManager.updateAuthState()
            }

            Log.i("LoginViewModel", if (success) "login success" else "login failed")
            onResult(success)
            _isLoading.value = false
        }
    }
}
