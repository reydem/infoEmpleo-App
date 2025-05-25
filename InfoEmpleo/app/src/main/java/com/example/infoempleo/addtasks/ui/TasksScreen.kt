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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import com.example.infoempleo.login.di.LocalAuthState    // ← añadir

import androidx.compose.material.icons.filled.Photo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {
    // Recupera el estado de autenticación (reclutador / postulante)
    val auth = LocalAuthState.current
    // Estado del diálogo
    val showDialog by tasksViewModel.showDialog.observeAsState(false)
    // Estado de errores
    val errorMessage by tasksViewModel.errorMessage.observeAsState()
    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbar en cambios de errorMessage
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            tasksViewModel.clearErrorMessage()
        }
    }

    // Estado del UI
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TasksUiState>(
        initialValue = TasksUiState.Loading,
        key1 = lifecycle,
        key2 = tasksViewModel
    ) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            tasksViewModel.uiState.collect { value = it }
            value
        }
    }

    // Estado de scroll
    val listState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { tasksViewModel.onShowDialogClick() }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir vacante")
            }
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1) saludo con zIndex para que quede por encima de la lista y del FAB
            Text(
                text = if (auth.esReclutador) "Bienvenido, reclutador" else "Bienvenido, postulante",
                
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .zIndex(1f),
                style = MaterialTheme.typography.titleMedium
            )


            when (uiState) {
                is TasksUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is TasksUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error cargando vacantes")
                    }
                }
                is TasksUiState.Success -> {
                    // Lista invertida
                    val tasks = (uiState as TasksUiState.Success).tasks.asReversed()

                    // Al cambiar la lista, hacer scroll a la primera vacante
                    LaunchedEffect(tasks) {
                        if (tasks.isNotEmpty()) {
                            listState.animateScrollToItem(0)
                        }
                    }

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(
                            top = 56.dp,      // deja espacio para el saludo
                            bottom = 80.dp,   // evita que el FAB tape la última tarjeta
                            start = 20.dp,
                            end = 20.dp
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex(0f)
                    ) {
                        items(tasks, key = { it.id }) { task ->
                            ItemTask(task, tasksViewModel)
                        }
                    }
                }
            }

            // Diálogo de añadir
            AddTasksDialog(
                show = showDialog,
                onDismiss = { tasksViewModel.onDialogClose() },
                onVacanteAdded = { vacanteModel, imageUri ->
                    tasksViewModel.onTasksCreated(vacanteModel, imageUri)
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
    onVacanteAdded: (TaskModel, Uri?) -> Unit
) {
    if (!show) return

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var salarioText by remember { mutableStateOf("") }

    // Estado para la imagen
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

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
                Spacer(Modifier.height(16.dp))

                // Selector de imagen
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { pickImageLauncher.launch("image/*") }) {
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = "Seleccionar imagen"
                        )
                    }
                    Text(
                        text = imageUri?.lastPathSegment ?: "Añadir foto de la empresa",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Vista previa de la imagen
                imageUri?.let { uri ->
                    Spacer(Modifier.height(8.dp))
                    AsyncImage(
                        model = uri,
                        contentDescription = "Preview imagen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val salario = salarioText.toDoubleOrNull() ?: 0.0
                        val nuevo = TaskModel(
                            id = System.currentTimeMillis().toString(),
                            title = titulo.trim(),
                            description = descripcion.trim(),
                            salary = salario,
                            imageUrl = null,
                            selected = false
                        )
                        onVacanteAdded(nuevo, imageUri)
                        // Reset campos
                        titulo = ""
                        descripcion = ""
                        salarioText = ""
                        imageUri = null
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