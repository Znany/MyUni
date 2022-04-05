package com.hfad.myuni.ui.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hfad.myuni.ui.dataClass.Subject
import com.hfad.myuni.ui.dataClass.Task
import com.hfad.myuni.ui.dataClass.WidgetData

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWidgetData(data: WidgetData)

    @Query("SELECT * FROM widget_data WHERE id = :my_id")
    suspend fun getWidgetData(my_id: Int): WidgetData
}