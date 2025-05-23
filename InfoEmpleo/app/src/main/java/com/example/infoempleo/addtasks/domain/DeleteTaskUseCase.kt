package com.example.infoempleo.addtasks.domain

import com.example.infoempleo.addtasks.data.TaskRepository
import com.example.infoempleo.addtasks.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.delete(taskModel)
    }

}