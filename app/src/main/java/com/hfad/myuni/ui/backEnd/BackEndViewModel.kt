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

    fun addTask(groupId: Int, subjectId: Int, header: String, description: String, dueDate: String) : Observable<Int> {
        return backEndRepository.addTask(groupId, subjectId, header, description, dueDate)
    }

    fun getSubjects(): Observable<JSONObject> {
        return backEndRepository.getSubjects()
    }

    fun getTimeTable(): Observable<JSONObject> {
        return backEndRepository.getTimeTable()
    }

    fun getInternetConnection(): Observable<Boolean>{
        return backEndRepository.checkInternetConnection()
    }
}