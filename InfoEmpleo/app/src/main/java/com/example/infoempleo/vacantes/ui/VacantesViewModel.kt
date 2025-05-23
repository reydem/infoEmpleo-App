// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/ui/VacantesViewModel.kt
package com.example.infoempleo.vacantes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoempleo.vacantes.domain.GetVacantesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class VacantesViewModel @Inject constructor(
  private val getVacantes: GetVacantesUseCase
) : ViewModel() {

  val vacantesUiState: StateFlow<VacantesUiState> =
    getVacantes()
      .map { VacantesUiState.Success(it) as VacantesUiState }
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VacantesUiState.Loading)
}
