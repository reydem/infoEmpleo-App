// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/data/network/UsuariosApi.kt
package com.example.infoempleo.usuarios.data.network

import retrofit2.http.GET

/**
 * API para obtener usuarios (candidatos) desde el backend.
 */
interface UsuariosApi {
    /**
     * Obtiene la lista de todos los usuarios registrados.
     */
    @GET("usuarios")
    suspend fun getUsuarios(): List<UsuarioDto>
}
