// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/domain/GetVacantesUseCase.kt
package com.example.infoempleo.vacantes.domain

import com.example.infoempleo.vacantes.data.VacantesRepository
import com.example.infoempleo.vacantes.data.network.VacanteDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVacantesUseCase @Inject constructor(
  private val repo: VacantesRepository
) {
  operator fun invoke(page: Int = 1, limit: Int = 20): Flow<List<VacanteDto>> =
    repo.getVacantes(page, limit)
}
