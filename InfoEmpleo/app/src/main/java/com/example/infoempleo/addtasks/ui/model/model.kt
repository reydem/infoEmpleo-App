package com.example.infoempleo.addtasks.ui.model

data class TaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val salary: Double,
    val imageUrl: String? = null,
    var selected: Boolean = false
)