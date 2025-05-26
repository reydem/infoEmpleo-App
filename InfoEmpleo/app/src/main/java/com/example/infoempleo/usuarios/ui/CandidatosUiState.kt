// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/ui/CandidatosUiState.kt
package com.example.infoempleo.usuarios.ui

import com.example.infoempleo.usuarios.data.network.UsuarioDto

/**
 * Estados de UI para la pantalla de candidatos.
 */
sealed interface CandidatosUiState {
    /** Se está cargando la lista de candidatos. */
    object Loading : CandidatosUiState

    /** Se obtuvieron exitosamente los candidatos. */
    data class Success(
        val candidatos: List<UsuarioDto>
    ) : CandidatosUiState

    /** Ocurrió un error al cargar los candidatos. */
    data class Error(
        val throwable: Throwable
    ) : CandidatosUiState
}
