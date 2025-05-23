package com.example.jotpackcomposelnstagram.addtasks.domain

import com.example.jotpackcomposelnstagram.addtasks.data.TaskRepository
import com.example.jotpackcomposelnstagram.addtasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.add(taskModel)
    }

}