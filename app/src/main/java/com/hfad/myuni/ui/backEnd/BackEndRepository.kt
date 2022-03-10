package com.hfad.myuni.ui.backEnd

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
            with(url.openConnection() as HttpURLConnection){
                requestMethod = "GET"

                val stringBuffer = StringBuffer()
                BufferedReader(InputStreamReader(inputStream)).use {
                    var inputLine = it.readLine()
                    while(inputLine != null){
                        stringBuffer.append(inputLine)
                        inputLine = it.readLine()
                    }
                }
                JSONObject(stringBuffer.toString())
            }
        }

    }
}