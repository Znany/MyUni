package com.hfad.myuni.ui.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.dataClass.Subject
import org.json.JSONArray

class TimeTableWidgetFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory{
    private var subjects: ArrayList<Subject> = ArrayList()
    private var subjectsHelperRoom: ArrayList<String> = ArrayList()
    private var subjectsHelperType: ArrayList<String> = ArrayList()
    private var backendViewModel: BackEndViewModel? = null

    override fun onCreate() {
        backendViewModel = BackEndViewModel(context.applicationContext as Application)
    }

    override fun onDataSetChanged() {
        Log.d("Widget", "onDataSetChanged")
        loadData()
    }

    override fun onDestroy() {subjects.clear()}

    override fun getCount(): Int {
        return subjects.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_widget).apply {
            setTextViewText(R.id.widget_item_label, subjects[p0].name)
            setTextViewText(R.id.widget_item_start, subjects[p0].start)
            setTextViewText(R.id.widget_item_end, subjects[p0].end)
            setTextViewText(R.id.widget_item_bottom_label, "${subjectsHelperRoom[p0]} (${subjectsHelperType[p0]})")
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_widget_loading)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return subjects[p0].id.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    private fun loadData(){
        val it = backendViewModel!!.getCurrentSubjects().blockingFirst()

        //subjects.add(Subject(0, "Bazy Danych", "9:00", "12:00", 1))
        //subjects.add(Subject(1, "Warsztat programisty", "13:00", "15:00", 1))
        Log.d("Widget", it.toString())
        val array: JSONArray = it.getJSONArray("resultset")
        val list: ArrayList<Subject> = ArrayList()
        val roomList: ArrayList<String> = ArrayList()
        val typeList: ArrayList<String> = ArrayList()
        for (i in 0 until array.length()){
            val name = array.getJSONObject(i).getString("name")
            val start = array.getJSONObject(i).getString("time_start").substring(0, 5)
            val end = array.getJSONObject(i).getString("time_end").substring(0, 5)
            val room = array.getJSONObject(i).getString("room")
            val type = array.getJSONObject(i).getString("lesson_type_name")
            Log.d("Widget", "$name, $start, $end")
            list.add(Subject(i, name, start, end, 0))
            roomList.add(room)
            typeList.add(type)
        }
        subjectsHelperType = typeList
        subjectsHelperRoom = roomList
        subjects = list
    }
}