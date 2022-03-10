package com.hfad.myuni.ui.timeTable

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R

class TimeTableAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lectures = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_table_empty, parent, false)
            EmptyTimeTableViewHolder(view)
        } else{
            //TODO CHANGE THIS VVV
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_table_empty, parent, false)
            EmptyTimeTableViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EmptyTimeTableViewHolder){
            Log.d("Adapter", "ok")
        }
    }

    override fun getItemCount(): Int {
        return if(lectures.size == 0){
            1
        } else{
            lectures.size
        }
    }

    class EmptyTimeTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}