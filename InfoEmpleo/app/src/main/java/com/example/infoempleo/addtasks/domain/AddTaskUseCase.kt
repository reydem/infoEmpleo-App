// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/domain/AddTaskUseCase.kt
package com.example.infoempleo.addtasks.domain

import com.example.infoempleo.addtasks.data.TaskRepository
import com.example.infoempleo.addtasks.ui.model.TaskModel
import java.io.File
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    /**
     * Crea una nueva vacante en el servidor.
     *
     * @param taskModel datos de la vacante a crear
     * @param imageFile archivo de imagen (o null si no hay)
     */
    suspend operator fun invoke(
        taskModel: TaskModel,
        imageFile: File? = null
    ) {
        taskRepository.add(taskModel, imageFile)
    }
}