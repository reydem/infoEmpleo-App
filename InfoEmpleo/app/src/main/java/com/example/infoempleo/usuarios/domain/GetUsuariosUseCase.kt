// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/domain/GetUsuariosUseCase.kt
package com.example.infoempleo.usuarios.domain

import com.example.infoempleo.usuarios.data.UsuariosRepository
import com.example.infoempleo.usuarios.data.network.UsuarioDto
import javax.inject.Inject

/**
 * Caso de uso para obtener la lista de usuarios (candidatos).
 */
class GetUsuariosUseCase @Inject constructor(
    private val repository: UsuariosRepository
) {
    /**
     * Ejecuta la obtención de todos los usuarios desde el repositorio.
     * @return Lista de UsuarioDto.
     */
    suspend operator fun invoke(): List<UsuarioDto> = repository.getAll()
}


