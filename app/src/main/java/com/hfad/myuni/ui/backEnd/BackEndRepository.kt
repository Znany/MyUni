package com.hfad.myuni.ui.backEnd

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BackEndRepository {
    fun getTasks() : Observable<JSONObject> {
        return Observable.fromCallable {
            val url = URL("http://51.77.58.66/api.php?option=tasks&&password=dupadupa321")
            urlConnectionToJSON(url, "GET")
        }
    }

    fun addTask(groupId: Int, subjectId: Int, header: String, description: String, dueDate: String) : Observable<Int>{
        return Observable.fromCallable {
            val url = URL(
                "http://51.77.58.66/api.php?" +
                        "option=add_task" +
                        "&&password=dupadupa321" +
                        "&&groupid=$groupId" +
                        "&&subject_id=$subjectId" +
                        "&&header=$header" +
                        "&&description=$description" +
                        "&&due_date=$dueDate")
            with(url.openConnection() as HttpURLConnection){
                Log.d("Repo", "Response: $responseMessage")
                val stringBuffer = StringBuffer()
                BufferedReader(InputStreamReader(inputStream)).use {
                    var inputLine = it.readLine()
                    while(inputLine != null){
                        stringBuffer.append(inputLine)
                        inputLine = it.readLine()
                    }
                }
                requestMethod = "GET"
                responseCode
            }
        }
    }

    fun getSubjects(): Observable<JSONObject> {
        return Observable.fromCallable {
            val url = URL("http://51.77.58.66/api.php?option=subjects&&password=dupadupa321")
            urlConnectionToJSON(url, "GET")
        }
    }

    fun getTimeTable(): Observable<JSONObject>{
        return Observable.fromCallable {
            val url = URL("http://51.77.58.66/api.php?option=timetable&&password=dupadupa321")
            urlConnectionToJSON(url, "GET")
        }
    }

    fun checkInternetConnection(): Observable<Boolean> {
        return Observable.fromCallable {
            val url = URL("https://www.google.com/")
            with(url.openConnection() as HttpURLConnection){
                connectTimeout = 3000
                requestMethod = "GET"
                responseCode == 200
            }
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