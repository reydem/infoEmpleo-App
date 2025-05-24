// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/ui/TasksScreen.kt
package com.example.infoempleo.addtasks.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.infoempleo.addtasks.ui.model.TaskModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {
    // Estado del diálogo
    val showDialog by tasksViewModel.showDialog.observeAsState(false)
    // Estado de errores (LiveData<String?> que debes exponer en tu VM)
    val errorMessage by tasksViewModel.errorMessage.observeAsState()
    // Para el Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Mostrar Snackbar cuando errorMessage cambie a no-null
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            // opcional: una vez mostrado, limpiar el mensaje
            tasksViewModel.clearErrorMessage()
        }
    }

    // Recoger el uiState desde el Flow del ViewModel
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TasksUiState>(
        initialValue = TasksUiState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel
    ) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect { value = it }
            value // para que el state se actualice
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { tasksViewModel.onShowDialogClick() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir vacante")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is TasksUiState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is TasksUiState.Error -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error cargando vacantes")
                    }
                }
                is TasksUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        items((uiState as TasksUiState.Success).tasks, key = { it.id }) { task ->
                            ItemTask(task, tasksViewModel)
                        }
                    }
                }
            }

            // Diálogo de añadir, siempre a mano pero solo visible si showDialog == true
            AddTasksDialog(
                show = showDialog,
                onDismiss = { tasksViewModel.onDialogClose() },
                onVacanteAdded = { vacanteModel ->
                    tasksViewModel.onTasksCreated(vacanteModel)
                }
            )
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, tasksViewModel: TasksViewModel) {

    LazyColumn {
        items(tasks, key = { it.id }) { task ->
           ItemTask(task, tasksViewModel)
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            // 1) Imagen (si existe)
            taskModel.imageUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Logo de ${taskModel.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(Modifier.height(8.dp))
            }

            // 2) Texto principal
            Text(text = taskModel.title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))

            // 3) Descripción
            Text(
                text = taskModel.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))

            // 4) Salario
            Text(text = "Salario: \$${taskModel.salary}")

            Spacer(Modifier.height(8.dp))

            // 5) Checkbox alineado a la derecha
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                Checkbox(
                    checked = taskModel.selected,
                    onCheckedChange = { tasksViewModel.onCheckBoxSelected(taskModel) }
                )
            }
        }
    }
}

@Composable
fun AddTasksDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onVacanteAdded: (TaskModel) -> Unit
) {
    if (!show) return

    // Estados locales para cada campo
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var salarioText by remember { mutableStateOf("") }
    var imagenUrl by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = CardDefaults.shape,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = "Añadir vacante",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = salarioText,
                    onValueChange = { salarioText = it },
                    label = { Text("Salario") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la imagen") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Convertir el salario o dejar 0.0 si es inválido
                        val salario = salarioText.toDoubleOrNull() ?: 0.0
                        // Crear modelo y notificar
                        onVacanteAdded(
                            TaskModel(
                                id = System.currentTimeMillis().toString(),
                                title = titulo.trim(),
                                description = descripcion.trim(),
                                salary = salario,
                                imageUrl = imagenUrl.ifBlank { null },
                                selected = false
                            )
                        )
                        // Limpiar y cerrar
                        titulo = ""
                        descripcion = ""
                        salarioText = ""
                        imagenUrl = ""
                        onDismiss()
                    },
                    enabled = titulo.isNotBlank() && descripcion.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir vacante")
                }
            }
        }
    }
}

@Composable
fun FabDialog(
    modifier: Modifier,
    tasksViewModel: TasksViewModel
) {
    FloatingActionButton(
        onClick = { tasksViewModel.onShowDialogClick() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Añadir"
        )
    }
}