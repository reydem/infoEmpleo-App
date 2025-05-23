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

@Composable
fun VacantesScreen(vm: VacantesViewModel) {
    val uiState by vm.vacantesUiState.collectAsState()

    when (uiState) {
        is VacantesUiState.Loading -> CircularProgressIndicator()
        is VacantesUiState.Error   -> Text("Error: ${(uiState as VacantesUiState.Error).error}")
        is VacantesUiState.Success -> {
            LazyColumn {
                items((uiState as VacantesUiState.Success).vacantes) { vac ->
                    ItemVacante(vac)
                }
            }
        }
    }
}

@Composable
fun ItemVacante(v: VacanteDto) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(text = v.titulo, fontWeight = FontWeight.Bold)
            Text(text = v.descripcion, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = "Salario: \$${v.salarioOfrecido}")
            // si quieres mostrar la imagen:
            // AsyncImage(model = "http://10.0.2.2:5000/${v.imagenEmpresa}", contentDescription = null)
        }
    }
}
