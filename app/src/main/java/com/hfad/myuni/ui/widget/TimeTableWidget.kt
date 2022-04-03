package com.hfad.myuni.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.hfad.myuni.R
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class TimeTableWidget : AppWidgetProvider() {
    private val ACTION_BACK = "ARROW_BACK"
    private val ACTION_FORWARD = "ARROW_FORWARD"
    private val values = ArrayList<Int>()

    private fun formatDay(current: Int): Int{
        if(current == 1 || current == 7){
            return 2
        }
        return current
    }

    private fun formatDayActionBack(current: Int): Int {
        if(current == 1 || current == 0){
            return 6
        }
        return current
    }

    private fun formatDayActionForward(current: Int): Int {
        if(current == 7){
            return 2
        }
        return current
    }

    private fun intentBroadcast(context: Context, action: String, appWidgetId: Int, index: Int): PendingIntent{
        val intentBack = Intent(context, TimeTableWidget::class.java)
        intentBack.action = action
        intentBack.putExtra("ID", appWidgetId)
        intentBack.putExtra("INDEX", index)
        return PendingIntent.getBroadcast(context, 0, intentBack, 0)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("Widget", "onUpdate")
        // There may be multiple widgets active, so update all of them
        var index = 0
        for (appWidgetId in appWidgetIds) {
           updateWidget(context, appWidgetId, appWidgetManager, "UPDATE", index)
            index += 1
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            val manager = AppWidgetManager.getInstance(context)
            if(intent.action == ACTION_BACK) {
                Toast.makeText(context, "BACK", Toast.LENGTH_LONG).show()
                if (context != null) {
                    updateWidget(context, intent.getIntExtra("ID", 0), manager, ACTION_BACK, intent.getIntExtra("INDEX", 0))
                }
                super.onReceive(context, intent)
            }
            if(intent.action == ACTION_FORWARD){
                if (context != null) {
                    updateWidget(context, intent.getIntExtra("ID", 0), manager, ACTION_FORWARD, intent.getIntExtra("INDEX", 0))
                }
                super.onReceive(context, intent)
            }
            super.onReceive(context, intent)
        }
        super.onReceive(context, intent)
    }

    private fun updateWidget(context: Context, appWidgetId: Int, appWidgetManager: AppWidgetManager, action: String, index: Int){
        val intToStringMap = mapOf(2 to "Poniedziałek", 3 to "Wtorek", 4 to "Środa", 5 to "Czwartek", 6 to "Piątek", 7 to "Poniedziałek", 1 to "Poniedziałek")
        var currentDay = formatDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))

        if(action == ACTION_BACK){
            currentDay = formatDayActionBack(currentDay - 1)
        }
        else if (action == ACTION_FORWARD){
            currentDay = formatDayActionForward(currentDay + 1)
        }

        val intent = Intent(context, TimeTableWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            putExtra("currentDay", currentDay - 1)
            data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
        }

        val views = RemoteViews(context.packageName, R.layout.time_table_widget).apply {
            setRemoteAdapter(R.id.widget_subject_list, intent)
            //Set on click listener
            setOnClickPendingIntent(R.id.widget_label_arrow_back, intentBroadcast(context, ACTION_BACK, appWidgetId, index))
            setOnClickPendingIntent(R.id.widget_label_arrow_forward, intentBroadcast(context, ACTION_FORWARD, appWidgetId, index))
        }

        if(values.size <= index){
            values.add(currentDay)
            views.setTextViewText(R.id.widget_subject_text, "${intToStringMap[currentDay]}")
        }
        else{
            if(action == ACTION_BACK){

            }
        }

        views.setTextViewText(R.id.widget_subject_text, "${intToStringMap[currentDay]}")

        /*
        if(values.size <= iterator){
            values.add(currentDay)
            view.setTextViewText(R.id.widget_subject_text, "${intToStringMap[currentDay]}")
        }
        else{
            when (action) {
                "ARROW_BACK" -> {
                    values[iterator] = formatDayActionBack(values[iterator] - 1)
                    view.setTextViewText(R.id.widget_subject_text, "${intToStringMap[values[iterator]]}")
                }
                "ARROW_FORWARD" -> {

                }
                else -> {
                    values[iterator] = currentDay
                    view.setTextViewText(R.id.widget_subject_text, "${intToStringMap[currentDay]}")
                }
            }
        }
        */

        appWidgetManager.updateAppWidget(appWidgetId, views)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_subject_list)
    }
}
