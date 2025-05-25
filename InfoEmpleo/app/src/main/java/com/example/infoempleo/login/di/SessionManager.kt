// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/di/SessionManager.kt
package com.example.infoempleo.login.di

import com.example.infoempleo.di.TokenPreferences
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
 */
@Singleton
class SessionManager @Inject constructor(
    private val tokenPrefs: TokenPreferences
) {
    /**
     * Recupera el estado de autenticación actual.
     */
    fun getAuthState(): AuthState {
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