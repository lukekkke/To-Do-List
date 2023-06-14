package com.example.todolist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist.data.TaskEntry
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskEntryDao {
    @Insert
    suspend fun insert(entry: TaskEntry)

    @Delete
    suspend fun delete(entry: TaskEntry)

    @Query("SELECT * FROM task_entries ORDER BY sortDays ASC")
    fun getAllDataSortedByDate(): Flow<List<TaskEntry>>


}