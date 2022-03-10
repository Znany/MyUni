package com.hfad.myuni.ui.backEnd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject

class BackEndViewModel(application: Application) : AndroidViewModel(application) {
    private var backEndRepository = BackEndRepository()

    fun getTasks() : Observable<JSONObject> {
        return backEndRepository.getTasks()
    }
}