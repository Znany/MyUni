package com.hfad.myuni.ui.dataClass

data class Task(val subject: String,
                val date: String,
                val description: String,
                val shortDescription: String,
                val id: Int,
                var isDone: Boolean)
