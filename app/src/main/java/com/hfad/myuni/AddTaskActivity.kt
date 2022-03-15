package com.hfad.myuni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import java.util.ArrayList

class AddTaskActivity : AppCompatActivity() {
    private var editTextHeader: EditText? = null
    private var editTextDescription: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        var addTaskButton : ImageButton = findViewById(R.id.add_task_confirm)
        editTextHeader = findViewById(R.id.add_task_edit_text_header)
        editTextDescription = findViewById(R.id.add_tasks_edit_text_description)
        val spinner: Spinner = findViewById(R.id.spinner)
        val backendViewModel = BackEndViewModel(application)

        backendViewModel.getSubjects().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
            val subjectList: ArrayList<String> = ArrayList()
            val array: JSONArray = it.getJSONArray("resultset")
            for (i in 0 until array.length()){
                val name = array.getJSONObject(i).getString("name")
                subjectList.add(name)
            }
            val adapter = ArrayAdapter(this, R.layout.item_spinner, subjectList)
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner.adapter = adapter
        }

    }

    private fun areViewsEmpty(): Boolean{
        return editTextHeader?.text?.isEmpty() == true  || editTextDescription?.text?.isEmpty() == true
    }
}