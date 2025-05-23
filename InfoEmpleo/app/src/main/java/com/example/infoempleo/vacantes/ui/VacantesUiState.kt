// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/ui/VacantesUiState.kt
package com.example.infoempleo.vacantes.ui

import com.example.infoempleo.vacantes.data.network.VacanteDto

sealed interface VacantesUiState {
  object Loading : VacantesUiState
  data class Success(val vacantes: List<VacanteDto>) : VacantesUiState
  data class Error(val error: Throwable) : VacantesUiState
}
