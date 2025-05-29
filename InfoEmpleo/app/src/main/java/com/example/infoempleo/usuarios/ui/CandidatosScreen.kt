// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/usuarios/ui/CandidatosScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)

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

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import coil.compose.AsyncImage
import com.example.infoempleo.usuarios.data.network.UsuarioDto

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.infoempleo.login.di.LocalAuthState








import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.infoempleo.addtasks.ui.AddTasksDialog
import com.example.infoempleo.addtasks.ui.TasksUiState
import com.example.infoempleo.addtasks.ui.TasksViewModel


@Composable
fun CandidatosScreen(
    candidatosViewModel: CandidatosViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    // --- Estado de candidatos ---
    val uiState by candidatosViewModel.uiState.collectAsState()
    val auth = LocalAuthState.current

    // --- Estado para añadir vacantes ---
    val tasksViewModel: TasksViewModel = hiltViewModel()
    val showDialog by tasksViewModel.showDialog.observeAsState(false)
    val errorMessage by tasksViewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val tasksUiState by tasksViewModel.uiState.collectAsState(initial = TasksUiState.Loading)

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            tasksViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candidatos") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Cerrar sesión")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (auth.esReclutador) {
                FloatingActionButton(
                    onClick = { tasksViewModel.onShowDialogClick() },
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir vacante")
                }
            }
        }
    ) { padding ->
        Column(modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .padding(padding)) {

            // Lista de candidatos
            when (uiState) {
                CandidatosUiState.Loading -> {
                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is CandidatosUiState.Error -> {
                    val error = (uiState as CandidatosUiState.Error).throwable
                    Text(
                        text = "Error cargando candidatos: ${error.localizedMessage}",
                        modifier = androidx.compose.ui.Modifier.padding(16.dp)
                    )
                }
                is CandidatosUiState.Success -> {
                    val candidatos = (uiState as CandidatosUiState.Success).candidatos
                    LazyColumn(
                        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
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

        // Diálogo para añadir vacante
        AddTasksDialog(
            show = showDialog,
            onDismiss = { tasksViewModel.onDialogClose() },
            onVacanteAdded = { vac, uri ->
                tasksViewModel.onTasksCreated(vac, uri)
            }
        )
    }
}

@Composable
private fun CandidatoItem(candidato: UsuarioDto) {
    Card(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
            candidato.fotoPerfilUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Foto de perfil de ${candidato.nombre}",
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            }
            Text(
                text = "${candidato.nombre} ${candidato.primerApellido}",
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
            Text(
                text = "Hoja de vida: ${candidato.hojaVidaPath ?: "No disponible"}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
        }
    }
}
