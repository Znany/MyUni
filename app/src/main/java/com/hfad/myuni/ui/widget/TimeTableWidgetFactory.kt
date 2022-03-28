package com.hfad.myuni.ui.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.hfad.myuni.R
import com.hfad.myuni.ui.dataClass.Subject

class TimeTableWidgetFactory(private val context: Context): RemoteViewsService.RemoteViewsFactory {
    var subjectList: ArrayList<Subject> = ArrayList()

    override fun onCreate() {
        val first = Subject(1, "Bazy Danych", "9:00", "10:00", 4)
        val second = Subject(1, "Warsztat programisty", "11:00", "12:00", 4)
        subjectList.add(first)
        subjectList.add(second)
        TODO("Not yet implemented")
    }

    override fun onDataSetChanged() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        return subjectList.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_subject).apply{
            setTextViewText(R.id.item_subject_label, subjectList[position].name)
            setTextViewText(R.id.item_subject_starting_hour, subjectList[position].start)
            setTextViewText(R.id.item_subject_ending_hour, subjectList[position].end)
        }
    }

    override fun getLoadingView(): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }
}