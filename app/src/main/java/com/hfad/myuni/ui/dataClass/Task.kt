package com.hfad.myuni.ui.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(val subject: String,
                val date: String,
                val description: String,
                val shortDescription: String,
                @PrimaryKey val id: Int,
                var isDone: Boolean)
