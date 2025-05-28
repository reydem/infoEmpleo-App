// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/ui/TasksScreen.kt
package com.example.infoempleo.addtasks.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.example.infoempleo.addtasks.ui.model.TaskModel
import com.example.infoempleo.login.di.LocalAuthState
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    tasksViewModel: TasksViewModel,
    onLogout: () -> Unit
) {
    val auth = LocalAuthState.current
    val showDialog by tasksViewModel.showDialog.observeAsState(false)
    val errorMessage by tasksViewModel.errorMessage.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Único uiState, con manejo de ciclo de vida interno
    val uiState by tasksViewModel.uiState
        .collectAsStateWithLifecycle(initialValue = TasksUiState.Loading)

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            tasksViewModel.clearErrorMessage()
        }
    }

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (auth.esReclutador) "Vacantes" else "Mis Postulaciones",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Cerrar sesión")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { tasksViewModel.onShowDialogClick() }) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir vacante")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = if (auth.esReclutador) "Bienvenido, reclutador" else "Bienvenido, postulante",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .zIndex(1f),
                style = MaterialTheme.typography.titleMedium
            )

            when (uiState) {
                TasksUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is TasksUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error cargando vacantes")
                    }
                }
                is TasksUiState.Success -> {
                    val tasks = (uiState as TasksUiState.Success).tasks.asReversed()
                    LaunchedEffect(tasks) {
                        if (tasks.isNotEmpty()) {
                            listState.animateScrollToItem(0)
                        }
                    }
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(
                            top = 56.dp,
                            bottom = 80.dp,
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

            AddTasksDialog(
                show = showDialog,
                onDismiss = { tasksViewModel.onDialogClose() },
                onVacanteAdded = { vac, uri -> tasksViewModel.onTasksCreated(vac, uri) }
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