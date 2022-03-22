package com.hfad.myuni.ui.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hfad.myuni.ui.dataClass.Subject
import com.hfad.myuni.ui.dataClass.Task

@Dao
interface DatabaseDao {
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<Task>

    @Query("SELECT * FROM subjects")
    suspend fun getSubjects(): List<Subject>

    @Query("DELETE FROM subjects")
    suspend fun clearAllSubjects(): Unit

    @Insert
    suspend fun insertSubjects(list: List<Subject>)
}