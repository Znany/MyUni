package com.hfad.myuni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView

class AddTaskActivity : AppCompatActivity() {
    private var editTextHeader: EditText? = null
    private var editTextDescription: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        var addTaskButton : ImageButton = findViewById(R.id.add_task_confirm)
        editTextHeader = findViewById(R.id.add_task_edit_text_header)
        editTextDescription = findViewById(R.id.add_tasks_edit_text_description)

    }

    private fun areViewsEmpty(): Boolean{
        return editTextHeader?.text?.isEmpty() == true  || editTextDescription?.text?.isEmpty() == true
    }
}