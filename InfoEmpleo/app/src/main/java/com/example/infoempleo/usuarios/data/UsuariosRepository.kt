// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/data/UsuariosRepository.kt
package com.example.infoempleo.usuarios.data

import javax.inject.Inject
import javax.inject.Singleton
import com.example.infoempleo.usuarios.data.network.UsuariosApi
import com.example.infoempleo.usuarios.data.network.UsuarioDto

/**
 * Repositorio para obtener datos de usuarios (candidatos) desde el backend.
 */
@Singleton
class UsuariosRepository @Inject constructor(
    private val api: UsuariosApi
) {
    /**
     * Obtiene la lista completa de usuarios registrados.
     */
    suspend fun getAll(): List<UsuarioDto> = api.getUsuarios()
}
