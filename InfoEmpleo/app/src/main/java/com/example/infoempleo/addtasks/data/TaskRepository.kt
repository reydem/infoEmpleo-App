// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/data/TaskRepository.kt
package com.example.infoempleo.addtasks.data

import com.example.infoempleo.addtasks.ui.model.TaskModel
import com.example.infoempleo.vacantes.data.network.VacantesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val vacantesApi: VacantesApi
) {

    val tasks: Flow<List<TaskModel>> = flow {
        val listaVacantes = vacantesApi.getTodasLasVacantes()
        emit(listaVacantes.map { dto ->
            TaskModel(
                id       = dto.id.hashCode(),
                task     = dto.titulo,
                selected = false
            )
        })
    }

    // <-- Estos métodos evitan el "Unresolved reference" en tus UseCase
    suspend fun add(taskModel: TaskModel) {
        // aquí podrías llamar a vacantesApi.postular(taskModel.id.toString())
    }

    suspend fun update(taskModel: TaskModel) {
        // aquí podrías llamar a vacantesApi.eliminarPostulacion(…)
    }

    suspend fun delete(taskModel: TaskModel) {
        // y aquí la lógica para eliminar una vacante si existiera
    }
}
