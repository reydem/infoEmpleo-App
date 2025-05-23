// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/vacantes/ui/VacantesScreen.kt
package com.example.infoempleo.vacantes.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.infoempleo.vacantes.data.network.VacanteDto
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip


@Composable
fun VacantesScreen(vm: VacantesViewModel) {
    val uiState by vm.vacantesUiState.collectAsState()

    when (uiState) {
        is VacantesUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is VacantesUiState.Error -> {
            Text(
                text = "Error: ${(uiState as VacantesUiState.Error).error.message}",
                modifier = Modifier.padding(16.dp)
            )
        }

        is VacantesUiState.Success -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items((uiState as VacantesUiState.Success).vacantes) { vac ->
                    ItemVacante(vac)
                }
            }
        }
    }
}

@Composable
fun ItemVacante(v: VacanteDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // Título
            Text(text = v.titulo, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(8.dp))

            // Imagen (si existe)
            v.imagenUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Logo de ${v.titulo}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
            }

            // Descripción
            Text(
                text = v.descripcion,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(8.dp))

            // Salario, reclutador y postulantes
            Text(text = "Salario: \$${v.salarioOfrecido}")
            Text(text = "Reclutador: ${v.reclutador}")
            Text(text = "Postulantes: ${v.postulantes.size}")

            Spacer(Modifier.height(8.dp))

            // Fechas
            Text(text = "Creada: ${v.createdAt.take(10)}")
            Text(text = "Actualizada: ${v.updatedAt.take(10)}")
        }
    }
}
