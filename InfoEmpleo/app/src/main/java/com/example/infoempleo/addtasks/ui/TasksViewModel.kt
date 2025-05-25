// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/ui/TasksViewModel.kt
package com.example.infoempleo.addtasks.ui

import android.net.Uri
import androidx.lifecycle.*
import com.example.infoempleo.addtasks.domain.AddTaskUseCase
import com.example.infoempleo.addtasks.domain.DeleteTaskUseCase
import com.example.infoempleo.addtasks.domain.GetTasksUseCase
import com.example.infoempleo.addtasks.domain.UpdateTaskUseCase
import com.example.infoempleo.addtasks.ui.model.TaskModel
import com.example.infoempleo.addtasks.ui.TasksUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    // Estado de la lista de vacantes
    val uiState: StateFlow<TasksUiState> = getTasksUseCase()
        .map { Success(it) as TasksUiState }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    // Control de la visibilidad del diálogo
    private val _showDialog = MutableLiveData(false)
    val showDialog: LiveData<Boolean> = _showDialog

    // Mensaje de error para mostrar en un Snackbar
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    /** Limpia el mensaje de error tras mostrarlo */
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /** Cierra el diálogo de añadir vacante */
    fun onDialogClose() {
        _showDialog.value = false
    }

    /** Abre el diálogo de añadir vacante */
    fun onShowDialogClick() {
        _showDialog.value = true
    }

    /**
     * Maneja la creación de una nueva vacante, incluyendo la imagen opcional.
     *
     * @param taskModel datos de la vacante a crear
     * @param imageUri  URI de la imagen seleccionada (o null si no hay)
     */
    fun onTasksCreated(taskModel: TaskModel, imageUri: Uri?) {
        _showDialog.value = false

        viewModelScope.launch {
            runCatching {
                // Llamada al caso de uso, que ahora acepta Uri? en lugar de File?
                addTaskUseCase(taskModel, imageUri)
            }.onFailure { throwable ->
                _errorMessage.value = throwable.message ?: "Error creando vacante"
            }
        }
    }

    /** Marca o desmarca una vacante como seleccionada */
    fun onCheckBoxSelected(taskModel: TaskModel) {
        viewModelScope.launch {
            updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
        }
    }

    /** Elimina una vacante */
    fun onItemRemove(taskModel: TaskModel) {
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}

