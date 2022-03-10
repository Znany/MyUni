package com.hfad.myuni.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.dataClass.Task
import com.hfad.myuni.ui.main.PageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.json.JSONObject

/**
 * A placeholder fragment containing a simple view.
 */
class TasksFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    lateinit var taskDisposable: Disposable;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.tasks_recycler)
        val layoutManager = LinearLayoutManager(this.context)
        val adapter = TasksAdapter()
        val backEndViewModel = BackEndViewModel(requireActivity().application)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        taskDisposable = backEndViewModel.getTasks().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe( {
            val jsonArray = it.getJSONArray("resultset")
            val taskList = mutableListOf<Task>()

            for (i in 0 until jsonArray.length()){
                val subject  = jsonArray.getJSONObject(i).getString("name")
                val description = jsonArray.getJSONObject(i).getString("description")
                val header = jsonArray.getJSONObject(i).getString("header")
                val dueDate = jsonArray.getJSONObject(i).getString("due_date")
                val id = jsonArray.getJSONObject(i).getInt("id")

                taskList.add(Task(subject, dueDate.substring(0, 10).replace("-", "."), description, header, id, false))
            }
            adapter.tasks = taskList
            adapter.sortTasks()
            adapter.notifyDataSetChanged()
        },
            {
                Log.d("TasksFragment", it.toString())
            }
        )


        val taskClick: PublishSubject<Int> = adapter.taskClickPublisher
        taskClick.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Toast.makeText(context, String.format("Clicked: %d", it), Toast.LENGTH_SHORT).show()
        }

        return root


    }

    override fun onDestroy() {
        if(!taskDisposable.isDisposed){
            taskDisposable.dispose()
        }
        super.onDestroy()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): TasksFragment {
            return TasksFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}