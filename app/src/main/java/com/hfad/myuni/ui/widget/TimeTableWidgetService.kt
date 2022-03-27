package com.hfad.myuni.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

class TimeTableWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TimeTableWidgetFactory(this.applicationContext, intent)
    }
}