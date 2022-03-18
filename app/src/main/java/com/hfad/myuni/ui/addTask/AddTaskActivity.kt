package com.hfad.myuni.ui.addTask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private val subjectList: ArrayList<String> = ArrayList()
    private val subjectIdList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val addTaskButton : ImageButton = findViewById(R.id.add_task_confirm)
        val editTextHeader: EditText = findViewById(R.id.add_task_edit_text_header)
        val editTextDescription: EditText = findViewById(R.id.add_tasks_edit_text_description)
        val datePicker: DatePicker = findViewById(R.id.add_task_date_picker)
        val spinner: Spinner = findViewById(R.id.spinner)
        val backendViewModel = BackEndViewModel(application)

        fun areViewsEmpty(): Boolean{
            return editTextHeader.text?.isEmpty() == true  || editTextDescription.text?.isEmpty() == true || spinner.selectedItem.toString().isEmpty()
        }

        fun getIdByName(name: String): Int {
            for (i in 0 until subjectList.size){
                if (name == subjectList[i]){
                    return subjectIdList[i].toInt()
                }
            }
            return -1
        }

        backendViewModel.getSubjects().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {

            val array: JSONArray = it.getJSONArray("resultset")
            for (i in 0 until array.length()){
                val name = array.getJSONObject(i).getString("name")
                val id = array.getJSONObject(i).getString("subject_id")
                val type = array.getJSONObject(i).getString("typ")

                subjectList.add("$name ($type)")
                Log.d("Id", "id_added $id")
                subjectIdList.add(id)
            }
            val adapter = ArrayAdapter(this, R.layout.item_spinner, subjectList)
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner.adapter = adapter
        }

        editTextHeader.doOnTextChanged { _, _, _, _ ->
            if(!areViewsEmpty()){
                addTaskButton.visibility = View.VISIBLE
            }
            else{
                addTaskButton.visibility = View.GONE
            }
        }

        editTextDescription.doOnTextChanged { _, _, _, _ ->
            if(!areViewsEmpty()){
                addTaskButton.visibility = View.VISIBLE
            }
            else{
                addTaskButton.visibility = View.GONE
            }
        }

        addTaskButton.setOnClickListener {
            backendViewModel.addTask(3, getIdByName(spinner.selectedItem.toString()), editTextHeader.text.toString(), editTextDescription.text.toString(), getDate(datePicker.dayOfMonth.toString(), datePicker.month.toString(), datePicker.year.toString())).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe ({
                if(it == 200){
                    Toast.makeText(application, "Dodano taska", Toast.LENGTH_LONG).show()
                }
            },
                {
                    Log.d("AddTaskActivity", it.toString())
                })
        }
    }

    private fun getDate(day: String, month: String, year: String): String{
        var formattedDay = day
        var formattedMonth = month
        if(day.length == 1){
            formattedDay = "0$day"
        }
        if(month.length == 1){
            formattedMonth = "0$month"
        }
        val stringBuffer = StringBuffer()
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())
        stringBuffer.append(year).append("-").append(formattedMonth).append("-").append(formattedDay).append(" ").append(currentDate)
        return stringBuffer.toString()
    }
}