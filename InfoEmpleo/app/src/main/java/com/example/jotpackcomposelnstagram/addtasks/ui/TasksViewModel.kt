package com.example.jotpackcomposelnstagram.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jotpackcomposelnstagram.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class TasksViewModel @Inject constructor():ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val task: List<TaskModel> = _tasks


    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTasksCreated(task: String) {
        _showDialog.value = false
        // pasamos explicitamente selected = false
        _tasks.add(TaskModel(task = task, selected = false))
    }


    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {

    }

}

//Log.i("Erik",task )