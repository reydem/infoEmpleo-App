// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/data/VacantesRepository.kt
package com.example.infoempleo.vacantes.data

import com.example.infoempleo.vacantes.data.network.VacantesApi
import com.example.infoempleo.vacantes.data.network.VacanteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VacantesRepository @Inject constructor(
  private val api: VacantesApi
) {
  fun getVacantes(page: Int, limit: Int): Flow<List<VacanteDto>> = flow {
    val response = api.getVacantesPaginadas(page, limit)
    emit(response.docs)
  }

  suspend fun getTodas(): List<VacanteDto> =
    api.getTodasLasVacantes()
}
