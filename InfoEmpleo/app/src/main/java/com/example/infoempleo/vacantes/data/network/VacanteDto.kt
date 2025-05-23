// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/data/network/VacanteDto.kt
package com.example.infoempleo.vacantes.data.network

import com.google.gson.annotations.SerializedName

data class VacanteDto(
    @SerializedName("_id")
    val id: String,

    val titulo: String,

    val descripcion: String,

    @SerializedName("salario_ofrecido")
    val salarioOfrecido: Double,

    @SerializedName("imagen_empresa")
    val imagenEmpresa: String? = null,

    val reclutador: String,

    val postulantes: List<String> = emptyList(),

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
) {
    /**
     * Devuelve la URL completa donde está alojada la imagen en el servidor.
     * Ajusta la base (`10.0.2.2:5000`) si tu backend corre en otra dirección.
     */
    val imagenUrl: String?
        get() = imagenEmpresa?.let { "http://10.0.2.2:5000/uploads/$it" }
}
