package com.example.todolist.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "task_entries")
data class TaskEntry(
    val title: String = "",
    val description: String = "",
    val createdDate: String,
    val dueDate: String,
    val location: String?,
    val sortDays: Long,
    @PrimaryKey(autoGenerate = true)
    val id :Long = 0
)
