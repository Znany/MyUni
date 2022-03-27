package com.hfad.myuni.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.hfad.myuni.R
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class TimeTableWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, TimeTableWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            val views = RemoteViews(context.packageName, R.layout.time_table_widget).apply {
                setRemoteAdapter(R.id.widget_subject_list, intent)
            }

            //appWidgetManager.updateAppWidget(appWidgetId, views)
            updateAppWidget(context, appWidgetManager, appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    view: RemoteViews
) {
    // Construct the RemoteViews object
    //val views = RemoteViews(context.packageName, R.layout.time_table_widget)
    val intToStringMap = mapOf(2 to "Poniedziałek", 3 to "Wtorek", 4 to "Środa", 5 to "Czwartek", 6 to "Piątek", 7 to "Poniedziałek", 1 to "Poniedziałek")
    val currentDayText = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    view.setTextViewText(R.id.widget_subject_text, "${intToStringMap[currentDayText]}")

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, view)
}