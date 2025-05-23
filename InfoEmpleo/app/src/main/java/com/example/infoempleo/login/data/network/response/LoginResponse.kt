package com.example.infoempleo.login.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
  val token: String?,
  @SerializedName("esReclutador") val esReclutador: Boolean = false,
  val correo: String?
)
