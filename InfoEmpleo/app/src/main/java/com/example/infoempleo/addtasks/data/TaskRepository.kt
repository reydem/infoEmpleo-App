// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/data/TaskRepository.kt
package com.example.infoempleo.addtasks.data

import com.example.infoempleo.addtasks.ui.model.TaskModel
import com.example.infoempleo.vacantes.data.network.VacantesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val vacantesApi: VacantesApi
) {
    // 1) SharedFlow que siempre reemite incluso si el valor es el mismo
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        // 2) emisión inicial
        refreshTrigger.tryEmit(Unit)
    }

    /**
     * Cada vez que refreshTrigger emita (incluyendo la inicial),
     * recarga la lista de vacantes desde el servidor.
     */
    val tasks: Flow<List<TaskModel>> = refreshTrigger
        .flatMapLatest {
            flow {
                val listaVac = withContext(Dispatchers.IO) {
                    vacantesApi.getTodasLasVacantes()
                }
                emit(listaVac.map { dto ->
                    TaskModel(
                        id          = dto.id,
                        title       = dto.titulo,
                        description = dto.descripcion,
                        salary      = dto.salarioOfrecido,
                        imageUrl    = dto.imagenUrl,
                        selected    = false
                    )
                })
            }
        }

    /**
     * Crea una nueva vacante en el servidor.
     * Al terminar, dispara una nueva recarga automática.
     */
    suspend fun add(taskModel: TaskModel, imageFile: File? = null) {
        withContext(Dispatchers.IO) {
            val tituloRB = taskModel.title
                .toRequestBody("text/plain".toMediaType())
            val descripcionRB = taskModel.description
                .toRequestBody("text/plain".toMediaType())
            val salarioRB = taskModel.salary
                .toString()
                .toRequestBody("text/plain".toMediaType())

            val imagenPart: MultipartBody.Part? = imageFile?.let { file ->
                val reqFile = file.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("imagen_empresa", file.name, reqFile)
            }

            val response = vacantesApi.createVacante(
                titulo          = tituloRB,
                descripcion     = descripcionRB,
                salarioOfrecido = salarioRB,
                imagen_empresa  = imagenPart
            )
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string().orEmpty()
                throw RuntimeException("Error creando vacante: ${response.code()} $errorBody")
            }
        }
        refreshTrigger.tryEmit(Unit)
    }

    /**
     * Actualiza una vacante existente en el servidor.
     * Al terminar, dispara una nueva recarga automática.
     */
    suspend fun update(taskModel: TaskModel, imageFile: File? = null) {
        withContext(Dispatchers.IO) {
            val tituloRB = taskModel.title
                .toRequestBody("text/plain".toMediaType())
            val descripcionRB = taskModel.description
                .toRequestBody("text/plain".toMediaType())
            val salarioRB = taskModel.salary
                .toString()
                .toRequestBody("text/plain".toMediaType())

            val imagenPart: MultipartBody.Part? = imageFile?.let { file ->
                val reqFile = file.asRequestBody("image/*".toMediaType())
                MultipartBody.Part.createFormData("imagen_empresa", file.name, reqFile)
            }

            val response = vacantesApi.actualizarVacante(
                id               = taskModel.id,
                titulo           = tituloRB,
                descripcion      = descripcionRB,
                salarioOfrecido  = salarioRB,
                imagen_empresa   = imagenPart
            )
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string().orEmpty()
                throw RuntimeException("Error actualizando vacante: ${response.code()} $errorBody")
            }
        }
        refreshTrigger.tryEmit(Unit)
    }

    /**
     * Elimina una vacante en el servidor.
     * Al terminar, dispara una nueva recarga automática.
     */
    suspend fun delete(taskModel: TaskModel) {
        withContext(Dispatchers.IO) {
            val response = vacantesApi.eliminarVacante(taskModel.id)
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string().orEmpty()
                throw RuntimeException("Error eliminando vacante: ${response.code()} $errorBody")
            }
        }
        refreshTrigger.tryEmit(Unit)
    }
}