// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/di/SessionManager.kt
package com.example.infoempleo.login.di

import com.example.infoempleo.di.TokenPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Representa el estado de autenticación de la sesión.
 *
 * @param token JWT actual (vacío si no hay)
 * @param auth true si hay un token no vacío
 * @param esReclutador flag que indica si el usuario es reclutador
 * @param correo correo del usuario autenticado
 */
data class AuthState(
    val token: String,
    val auth: Boolean,
    val esReclutador: Boolean,
    val correo: String
)

/**
 * Gestiona la sesión del usuario leyendo y escribiendo en TokenPreferences.
 * Expone un flujo reactivo de AuthState para poder observar cambios en Compose.
 */
@Singleton
class SessionManager @Inject constructor(
    private val tokenPrefs: TokenPreferences
) {
    // StateFlow interno con el estado de autenticación
    private val _authState: MutableStateFlow<AuthState> =
        MutableStateFlow(loadAuthState())

    /** Flujo público que emite el estado de autenticación */
    val authState: StateFlow<AuthState> = _authState

    /**
     * Recarga el estado de autenticación leyendo de TokenPreferences
     * y notifica a todos los observadores.
     */
    fun updateAuthState() {
        _authState.value = loadAuthState()
    }

    /**
     * Lee las preferencias y construye un AuthState inicial o actualizado.
     */
    private fun loadAuthState(): AuthState {
        val token = tokenPrefs.getJwtToken().orEmpty()
        val isAuth = token.isNotBlank()
        val reclutador = tokenPrefs.isReclutador()
        val correo = tokenPrefs.getCorreo().orEmpty()

        return AuthState(
            token = token,
            auth = isAuth,
            esReclutador = reclutador,
            correo = correo
        )
    }
}