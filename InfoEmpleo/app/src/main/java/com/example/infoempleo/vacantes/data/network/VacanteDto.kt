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
)