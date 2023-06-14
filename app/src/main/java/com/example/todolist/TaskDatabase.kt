package com.example.todolist

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.TaskEntry

@Database (entities = [TaskEntry::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskEntryDao(): TaskEntryDao
}