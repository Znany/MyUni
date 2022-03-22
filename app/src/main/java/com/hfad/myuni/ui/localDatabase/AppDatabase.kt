package com.hfad.myuni.ui.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.myuni.ui.dataClass.Subject
import com.hfad.myuni.ui.dataClass.Task

@Database(version = 1, entities = [Task::class, Subject::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}