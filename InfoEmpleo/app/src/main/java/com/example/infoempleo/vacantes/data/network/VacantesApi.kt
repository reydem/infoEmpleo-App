package com.example.infoempleo.vacantes.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Path

import com.example.infoempleo.vacantes.data.network.VacanteDto
import com.example.infoempleo.vacantes.data.network.PaginatedResponse


interface VacantesApi {
    @GET("vacantes/pagination")
    suspend fun getVacantesPaginadas(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponse<VacanteDto>

    @GET("vacantes")
    suspend fun getTodasLasVacantes(): List<VacanteDto>

    @POST("vacantes/{idVacante}/postular")
    suspend fun postular(
        @Path("idVacante") id: String
    ): Response<Unit>
    // … y así sucesivamente
}