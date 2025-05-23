package com.example.infoempleo.addtasks.domain

import com.example.infoempleo.addtasks.data.TaskRepository
import com.example.infoempleo.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = taskRepository.tasks
}