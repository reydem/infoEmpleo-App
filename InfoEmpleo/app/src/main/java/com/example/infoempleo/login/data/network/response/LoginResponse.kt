// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/login/data/network/response/LoginResponse.kt
package com.example.infoempleo.login.data.network.response

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta del endpoint de login:
 * - token: JWT para autorizar peticiones posteriores
 * - esReclutador: flag que indica si el usuario es reclutador
 * - correo: email del usuario autenticado
 */
data class LoginResponse(
    @SerializedName("token")
    val token: String?,

    @SerializedName("esReclutador")
    val esReclutador: Boolean = false,

    @SerializedName("correo")
    val correo: String? = null
)
