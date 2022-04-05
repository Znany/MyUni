package com.hfad.myuni.ui.dataClass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="widget_data")
data class WidgetData(@PrimaryKey val id: Int, var currentDay: Int)
