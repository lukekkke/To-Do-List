package com.example.todolist

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TaskEntry
import com.example.todolist.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
class TaskViewModel (val database: TaskDatabase) : ViewModel() {

    fun addEntry(title: String,
                 description: String,
                 createdDate: String,
                 dueDate: String,
                 location: String,
                 sortDays: Long
        ){
        viewModelScope.launch{
            val entry = TaskEntry(title = title,
                description = description,
                createdDate = createdDate,
                dueDate = dueDate,
                location = location,
                sortDays = sortDays
                )
            database.taskEntryDao().insert(entry)
        }
    }
    fun delete(entry: TaskEntry){
        viewModelScope.launch{
            database.taskEntryDao().delete(entry)
        }
    }
}