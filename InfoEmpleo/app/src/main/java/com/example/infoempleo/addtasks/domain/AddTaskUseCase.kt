// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/domain/AddTaskUseCase.kt
package com.example.infoempleo.addtasks.domain

import android.net.Uri
import com.example.infoempleo.addtasks.data.TaskRepository
import com.example.infoempleo.addtasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    /**
     * Crea una nueva vacante en el servidor.
     *
     * @param taskModel datos de la vacante a crear
     * @param imageUri URI de la imagen seleccionada (o null si no hay)
     */
    suspend operator fun invoke(
        taskModel: TaskModel,
        imageUri: Uri? = null
    ) {
        taskRepository.add(taskModel, imageUri)
    }
}
