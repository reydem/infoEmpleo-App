package com.example.jotpackcomposelnstagram.addtasks.ui



import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.DefaultTab.AlbumsTab.value
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TasksScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        AddTasksDialog(true){}
        FabDialog(Modifier.align(Alignment.BottomEnd))

    }
}

@Composable
fun FabDialog(modifier: Modifier) {
    FloatingActionButton(onClick = {
        //mostrar dialogo
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
fun AddTasksDialog(show: Boolean, onDismiss: () -> Unit) {
    var myTask by remember { mutableStateOf("") }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)) {
                Text(
                    text = "Añade tu tarea",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = { myTask = it })
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        //mandar tarea
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir tarea")
                }
            }
        }
    }
}
