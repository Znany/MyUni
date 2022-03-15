package com.hfad.myuni.ui.backEnd

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable

class BackEndRepository {
    fun getTasks() : Observable<JSONObject> {
        return Observable.fromCallable {
            val url = URL("http://51.77.58.66/api.php?option=tasks&&password=dupadupa321")
            urlConnectionToJSON(url, "GET")
        }
    }

    fun addTask(groupId: Int, subjectId: Int, header: String, description: String, dueDate: String) : Observable<JSONObject>{
        return Observable.fromCallable {
            val url = URL(
                "http://51.77.58.66/api.php?" +
                        "option=add_task" +
                        "&&password=dupadupa321" +
                        "&&groupid=$groupId" +
                        "&&subject_id=$subjectId" +
                        "&&header=$header" +
                        "description=$description" +
                        "due_date=$dueDate")
            urlConnectionToJSON(url, "GET")
        }
    }

    fun getSubjects(): Observable<JSONObject> {
        return Observable.fromCallable {
            val url = URL("http://51.77.58.66/api.php?option=subjects&&password=dupadupa321")
            urlConnectionToJSON(url, "GET")
        }
    }

    private fun urlConnectionToJSON(url: URL, method: String): JSONObject{
        with(url.openConnection() as HttpURLConnection){
            requestMethod = method

            val stringBuffer = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while(inputLine != null){
                    stringBuffer.append(inputLine)
                    inputLine = it.readLine()
                }
            }
            return JSONObject(stringBuffer.toString())
        }
    }
}