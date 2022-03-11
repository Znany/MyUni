package com.hfad.myuni.ui.tasks

import android.annotation.SuppressLint
import android.util.Log
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
    var tasks = mutableListOf<Task>()
    var hasDataLoaded = false

    var taskClickPublisher: PublishSubject<Task> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tasks_empty, parent, false)
            EmptyTaskViewHolder(view)
        } else if (viewType == 1) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tasks, parent, false)
            TaskViewHolder(view)
        }
        else{
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_tasks_loading, parent, false)
            EmptyTaskViewHolder(view)
        }
    }

       @SuppressLint("NotifyDataSetChanged")
       override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TaskViewHolder){
            Log.d("Adapter", String.format("lIST SIZE: %d", tasks.size))
            holder.subjectTextView.text = tasks[position].subject
            holder.dateTextView.text = tasks[position].date
            holder.shortDescriptionTextView.text = tasks[position].shortDescription

            holder.markAsDone.setOnClickListener {
                tasks[position].isDone = true
                taskClickPublisher.onNext(tasks[position])
                tasks.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                Log.d("Adapter", "Clicked $position")
            }
        }
    }

    override fun getItemCount(): Int {
        if(tasks.size == 0){
            return 1;
        }
        return tasks.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(!hasDataLoaded){
            return -1
        }
        else if(tasks.size == 0){
            0
        } else{
            1
        }
    }
}

class EmptyTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

}

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val subjectTextView: TextView = itemView.findViewById(R.id.item_tasks_subject)
    val shortDescriptionTextView: TextView = itemView.findViewById(R.id.item_tasks_short_description)
    val dateTextView: TextView = itemView.findViewById(R.id.item_tasks_date)
    val markAsDone: ImageView = itemView.findViewById(R.id.item_tasks_mark_as_done)
}