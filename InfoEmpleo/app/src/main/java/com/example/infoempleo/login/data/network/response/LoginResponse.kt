// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/data/network/response/LoginResponse.kt
// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/data/network/response/LoginResponse.kt
package com.example.infoempleo.login.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta del endpoint de login:
 * - token: JWT para autorizar peticiones posteriores
 * - usuario: objeto con todos los datos del usuario autenticado
 */
data class LoginResponse(
    @SerializedName("token")
    val token: String?,

    @SerializedName("usuario")
    val usuario: Usuario? = null
)

/**
 * Modelo que refleja el esquema Usuarios de MongoDB:
 * - nombre, apellidos, correo, teléfono, foto de perfil, hoja de vida
 * - flag esReclutador
 * - lista de postulaciones (vacante + estado)
 */
data class Usuario(
    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("primerApellido")
    val primerApellido: String?,

    @SerializedName("segundoApellido")
    val segundoApellido: String?,

    @SerializedName("correo")
    val correo: String?,

    @SerializedName("telefono")
    val telefono: String?,

    @SerializedName("fotoPerfil")
    val fotoPerfil: String?,

    @SerializedName("hojaVida")
    val hojaVida: String?,

    @SerializedName("esReclutador")
    val esReclutador: Boolean = false,

    @SerializedName("postulaciones")
    val postulaciones: List<Postulacion>? = null
)

/**
 * Cada elemento de la lista de postulaciones:
 * - vacante: _id de la vacante referenciada
 * - estado: "aplicado" o "cancelado"
 */
data class Postulacion(
    @SerializedName("vacante")
    val vacante: String?,

    @SerializedName("estado")
    val estado: String?
)
