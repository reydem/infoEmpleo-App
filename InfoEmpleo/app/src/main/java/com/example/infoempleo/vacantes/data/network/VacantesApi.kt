// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/data/network/VacantesApi.kt
package com.example.infoempleo.vacantes.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface VacantesApi {

    @GET("vacantes/pagination")
    suspend fun getVacantesPaginadas(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponse<VacanteDto>

    @GET("vacantes")
    suspend fun getTodasLasVacantes(): List<VacanteDto>

    @Multipart
    @POST("vacantes")
    suspend fun createVacante(
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("salario_ofrecido") salarioOfrecido: RequestBody,
        @Part imagen_empresa: MultipartBody.Part? = null
    ): Response<VacanteDto>

    @POST("vacantes/{idVacante}/postular")
    suspend fun postular(
        @Path("idVacante") id: String
    ): Response<Unit>

    @DELETE("vacantes/{idVacante}/postular")
    suspend fun eliminarPostulacion(
        @Path("idVacante") id: String
    ): Response<Unit>

    @Multipart
    @PUT("vacantes/{idVacante}")
    suspend fun actualizarVacante(
        @Path("idVacante") id: String,
        @Part("titulo") titulo: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("salario_ofrecido") salarioOfrecido: RequestBody,
        @Part imagen_empresa: MultipartBody.Part? = null
    ): Response<VacanteDto>

    @DELETE("vacantes/{idVacante}")
    suspend fun eliminarVacante(
        @Path("idVacante") id: String
    ): Response<Unit>
}


