package com.hfad.myuni.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.dataClass.Task

class TasksDoneAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var tasksCompleted = mutableListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tasks_done, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){
            Log.d("Adapter", String.format("lIST SIZE: %d", tasksCompleted.size))
            holder.subjectTextView.text = tasksCompleted[position].subject
            holder.dateTextView.text = tasksCompleted[position].date
            holder.shortDescriptionTextView.text = tasksCompleted[position].shortDescription
        }
    }

    override fun getItemCount(): Int {
        return tasksCompleted.size
    }
}