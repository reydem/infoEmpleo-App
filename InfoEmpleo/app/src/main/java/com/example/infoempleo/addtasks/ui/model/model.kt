// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/addtasks/ui/model/model.kt

package com.example.infoempleo.addtasks.ui.model

data class TaskModel(
    val id: String,           // antes Int, ahora String
    val title: String,
    val description: String,
    val salary: Double,
    val imageUrl: String? = null,
    var selected: Boolean = false
)