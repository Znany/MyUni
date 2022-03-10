package com.hfad.myuni.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.dataClass.Task
import io.reactivex.rxjava3.subjects.PublishSubject

class TasksAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tasks = mutableListOf<Task>()

    var taskClickPublisher: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tasks, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){
            holder.subjectTextView.text = tasks[position].subject
            holder.dateTextView.text = tasks[position].date
            holder.shortDescriptionTextView.text = tasks[position].shortDescription

            holder.markAsDone.setOnClickListener {
                taskClickPublisher.onNext(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun setTaskList(tasks: MutableList<Task>){
        this.tasks = tasks
    }


}

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val subjectTextView: TextView = itemView.findViewById(R.id.item_tasks_subject)
    val shortDescriptionTextView: TextView = itemView.findViewById(R.id.item_tasks_short_description)
    val dateTextView: TextView = itemView.findViewById(R.id.item_tasks_date)
    val markAsDone: ImageView = itemView.findViewById(R.id.item_tasks_mark_as_done)
}