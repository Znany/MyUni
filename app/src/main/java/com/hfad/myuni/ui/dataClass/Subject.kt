package com.hfad.myuni.ui.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(@PrimaryKey val id: Int, val name: String, val start: String, val end: String, val day: Int)
