// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/data/network/UsuarioDto.kt
package com.example.infoempleo.usuarios.data.network

import com.google.gson.annotations.SerializedName

/**
 * DTO que representa un usuario (candidato) desde el backend.
 */
data class UsuarioDto(
    @SerializedName("_id")
    val id: String,

    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String?,
    val correo: String,
    val telefono: String,
    val fotoPerfil: String?,   // nombre de archivo en el servidor
    val hojaVida: String?      // nombre de archivo en el servidor
) {
    /**
     * URL completa de la foto de perfil, si existe.
     */
    val fotoPerfilUrl: String?
        get() = fotoPerfil?.let { "http://10.0.2.2:5000/uploads/$it" }

    /**
     * Ruta o nombre de la hoja de vida.
     */
    val hojaVidaPath: String?
        get() = hojaVida
}


