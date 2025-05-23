package com.example.jotpackcomposelnstagram.addtasks.domain

import com.example.jotpackcomposelnstagram.addtasks.data.TaskRepository
import com.example.jotpackcomposelnstagram.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = taskRepository.tasks
}