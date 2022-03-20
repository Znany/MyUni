package com.hfad.myuni.ui.localDatabase

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

class DatabaseBuilder {
    private var database : AppDatabase? = null

    fun getDb(context: Context): AppDatabase{
        return if(database == null){
            database = Room.databaseBuilder(context, AppDatabase::class.java, "my_db").build()
            database as AppDatabase
        } else{
            database as AppDatabase
        }
    }
}