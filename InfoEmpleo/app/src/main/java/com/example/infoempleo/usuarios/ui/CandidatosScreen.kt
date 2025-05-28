// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/ui/CandidatosScreen.kt
package com.example.infoempleo.usuarios.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.infoempleo.usuarios.data.network.UsuarioDto
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Pantalla que muestra la lista de candidatos (usuarios).
 */
@Composable
fun CandidatosScreen(
    viewModel: CandidatosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        CandidatosUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is CandidatosUiState.Error -> {
            val error = (uiState as CandidatosUiState.Error).throwable
            Text(
                text = "Error cargando candidatos: ${error.localizedMessage}",
                modifier = Modifier.padding(16.dp)
            )
        }

        is CandidatosUiState.Success -> {
            val candidatos = (uiState as CandidatosUiState.Success).candidatos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(candidatos, key = { it.id }) { candidato ->
                    CandidatoItem(candidato)
                }
            }
        }
    }
}

@Composable
private fun CandidatoItem(candidato: UsuarioDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),                           // igual que ItemTask
        elevation = CardDefaults.cardElevation(4.dp) // mismo grosor de sombra
    ) {
        Column(Modifier.padding(16.dp)) {            // mismo padding interior

            // 1) Imagen de perfil (si existe)
            candidato.fotoPerfilUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Foto de perfil de ${candidato.nombre}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
            }

            // 2) Nombre del candidato
            Text(
                text = "${candidato.nombre} ${candidato.primerApellido}",
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))

            // 3) Hoja de vida (comportamiento de texto idéntico al de descripción en ItemTask)
            Text(
                text = "Hoja de vida: ${candidato.hojaVidaPath ?: "No disponible"}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))

            // 4) (Opcional) Podrías añadir aquí botones o iconos de acción, alineados con un Row
            //    de la misma forma que el Checkbox en ItemTask.
        }
    }
}