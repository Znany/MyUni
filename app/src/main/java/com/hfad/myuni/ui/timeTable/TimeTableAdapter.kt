package com.hfad.myuni.ui.timeTable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.dataClass.Subject

class TimeTableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lectures = mutableListOf<Subject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_table_empty, parent, false)
            EmptyTimeTableViewHolder(view)
        } else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subject, parent, false)
            TimeTableViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("Adapter", String.format("Lectures: %d", lectures.size))
        if(holder is TimeTableViewHolder){
            Log.d("Adapter", String.format("name: %s", lectures[position].name))
            holder.label.text = lectures[position].name
            holder.end.text = lectures[position].end.substring(0, 5)
            holder.start.text = lectures[position].start.substring(0, 5)
            if(position == lectures.size - 1){
                holder.bar.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return if(lectures.size == 0){
            1
        } else{
            lectures.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (lectures.size == 0){
            0
        } else{
            1
        }
    }

    class EmptyTimeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    class TimeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val label: TextView = itemView.findViewById(R.id.item_subject_label)
        val end: TextView = itemView.findViewById(R.id.item_subject_ending_hour)
        val start: TextView = itemView.findViewById(R.id.item_subject_starting_hour)
        val bar: View = itemView.findViewById(R.id.item_subject_bottom_bar)
    }
}