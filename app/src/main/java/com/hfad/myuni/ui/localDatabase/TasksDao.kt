package com.hfad.myuni.ui.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hfad.myuni.ui.dataClass.Task
import org.json.JSONObject

@Dao
interface TasksDao {
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<Task>
}