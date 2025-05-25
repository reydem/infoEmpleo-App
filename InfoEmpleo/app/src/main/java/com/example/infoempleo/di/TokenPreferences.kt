// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/di/TokenPreferences.kt
package com.example.infoempleo.di

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    /**
     * Guarda el JWT para autorizar futuras peticiones.
     */
    fun saveJwtToken(token: String) {
        prefs.edit().putString(KEY_JWT, token).apply()
    }

    /**
     * Recupera el JWT almacenado (o null si no existe).
     */
    fun getJwtToken(): String? =
        prefs.getString(KEY_JWT, null)

    /**
     * Guarda el flag que indica si el usuario es reclutador.
     */
    fun saveEsReclutador(isReclutador: Boolean) {
        prefs.edit().putBoolean(KEY_RECLUTADOR, isReclutador).apply()
    }

    /**
     * Consulta si el usuario es reclutador (por defecto false).
     */
    fun isReclutador(): Boolean =
        prefs.getBoolean(KEY_RECLUTADOR, false)

    /**
     * Guarda el correo del usuario autenticado.
     */
    fun saveCorreo(correo: String) {
        prefs.edit().putString(KEY_CORREO, correo).apply()
    }

    /**
     * Recupera el correo almacenado (o null si no existe).
     */
    fun getCorreo(): String? =
        prefs.getString(KEY_CORREO, null)

    private companion object {
        const val KEY_JWT = "jwt_token"
        const val KEY_RECLUTADOR = "es_reclutador"
        const val KEY_CORREO = "correo"
    }
}