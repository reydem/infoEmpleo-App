// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/data/TaskRepository.kt
package com.example.infoempleo.addtasks.data

import android.content.Context
import android.net.Uri
import com.example.infoempleo.addtasks.ui.model.TaskModel
import com.example.infoempleo.vacantes.data.network.VacantesApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val vacantesApi: VacantesApi,
    @ApplicationContext private val context: Context
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    init {
        refreshTrigger.tryEmit(Unit)
    }

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
     * @param taskModel datos de la vacante
     * @param imageUri  URI de la imagen seleccionada (o null)
     */
    suspend fun add(taskModel: TaskModel, imageUri: Uri? = null) {
        withContext(Dispatchers.IO) {
            val tituloRB = taskModel.title
                .toRequestBody("text/plain".toMediaType())
            val descripcionRB = taskModel.description
                .toRequestBody("text/plain".toMediaType())
            val salarioRB = taskModel.salary
                .toString()
                .toRequestBody("text/plain".toMediaType())

            // Preparamos la parte de la imagen con su MIME real
            val imagenPart: MultipartBody.Part? = imageUri?.let { uri ->
                val input = context.contentResolver.openInputStream(uri)
                    ?: throw IOException("No se pudo leer URI $uri")
                val bytes = input.readBytes().also { input.close() }

                val mimeType = context.contentResolver.getType(uri)
                    ?: "image/jpeg"  // fallback seguro
                val body = bytes.toRequestBody(mimeType.toMediaType())

                val filename = uri.lastPathSegment ?: "imagen.jpg"
                MultipartBody.Part.createFormData(
                    name = "imagen_empresa",
                    filename = filename,
                    body = body
                )
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
     * Actualiza una vacante existente.
     * @param taskModel datos de la vacante
     * @param imageUri  URI de la imagen (o null)
     */
    suspend fun update(taskModel: TaskModel, imageUri: Uri? = null) {
        withContext(Dispatchers.IO) {
            val tituloRB = taskModel.title
                .toRequestBody("text/plain".toMediaType())
            val descripcionRB = taskModel.description
                .toRequestBody("text/plain".toMediaType())
            val salarioRB = taskModel.salary
                .toString()
                .toRequestBody("text/plain".toMediaType())

            val imagenPart: MultipartBody.Part? = imageUri?.let { uri ->
                val input = context.contentResolver.openInputStream(uri)
                    ?: throw IOException("No se pudo leer URI $uri")
                val bytes = input.readBytes().also { input.close() }

                val mimeType = context.contentResolver.getType(uri)
                    ?: "image/jpeg"
                val body = bytes.toRequestBody(mimeType.toMediaType())

                val filename = uri.lastPathSegment ?: "imagen.jpg"
                MultipartBody.Part.createFormData(
                    name = "imagen_empresa",
                    filename = filename,
                    body = body
                )
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

