// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/ui/CandidatosViewModel.kt
package com.example.infoempleo.usuarios.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infoempleo.usuarios.domain.GetUsuariosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel para manejar la carga de candidatos (usuarios) desde el repositorio.
 */
@HiltViewModel
class CandidatosViewModel @Inject constructor(
    private val getUsuariosUseCase: GetUsuariosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CandidatosUiState>(CandidatosUiState.Loading)
    val uiState: StateFlow<CandidatosUiState> = _uiState.asStateFlow()

    init {
        loadCandidatos()
    }

    /**
     * Carga la lista de candidatos y actualiza el estado de la UI.
     */
    fun loadCandidatos() {
        viewModelScope.launch {
            _uiState.value = CandidatosUiState.Loading
            runCatching {
                getUsuariosUseCase()
            }.onSuccess { lista ->
                _uiState.value = CandidatosUiState.Success(lista)
            }.onFailure { error ->
                _uiState.value = CandidatosUiState.Error(error)
            }
        }
    }
}

