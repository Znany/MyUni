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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class TimeTableWidgetFactory(private val context: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory{
    private var subjects: ArrayList<Subject> = ArrayList()
    private var backendViewModel: BackEndViewModel? = null

    override fun onCreate() {
        backendViewModel = BackEndViewModel(context.applicationContext as Application)
    }

    override fun onDataSetChanged() {
        loadData()
    }

    override fun onDestroy() {subjects.clear()}

    override fun getCount(): Int {
        Log.d("Widget", "getCount: ${subjects.size}")
        return subjects.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_widget).apply {
            setTextViewText(R.id.widget_item_label, subjects[p0].name)
            setTextViewText(R.id.widget_item_start, subjects[p0].start)
            setTextViewText(R.id.widget_item_end, subjects[p0].end)
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
        for (i in 0 until array.length()){
            val name = array.getJSONObject(i).getString("name")
            val start = array.getJSONObject(i).getString("time_start").substring(0, 5)
            val end = array.getJSONObject(i).getString("time_end").substring(0, 5)
            Log.d("Widget", "$name, $start, $end")
            list.add(Subject(i, name, start, end, 0))
        }
        subjects = list
    }
}