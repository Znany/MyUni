package com.hfad.myuni.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.dataClass.Task
import com.hfad.myuni.ui.main.ConnectionViewModel
import com.hfad.myuni.ui.main.PageViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

class TasksFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    lateinit var taskDisposable: Disposable;
    var isTasksTabCollapsed = false
    var isCompletedTasksTabCollapsed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java].apply {
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.tasks_recycler)
        val completedRecyclerView: RecyclerView = root.findViewById(R.id.tasks_done_recycler)
        val layoutManager = LinearLayoutManager(this.context)
        val completedLayoutManager = LinearLayoutManager(this.context)
        //This recycler starts as collapsed
        completedRecyclerView.visibility = View.GONE
        val adapter = TasksAdapter()
        val completedAdapter = TasksDoneAdapter()
        val backEndViewModel = BackEndViewModel(requireActivity().application)

        val tasksTab: ConstraintLayout = root.findViewById(R.id.tab_tasks_not_done)
        val completedTasksTab: ConstraintLayout = root.findViewById(R.id.tab_tasks_done)

        val noConnectionWrapper: ConstraintLayout = root.findViewById(R.id.tasks_no_connection_wrapper)

        completedRecyclerView.layoutManager = completedLayoutManager
        recyclerView.layoutManager  = layoutManager
        recyclerView.adapter = adapter
        completedRecyclerView.adapter = completedAdapter

        val tab = Tab(tasksTab, recyclerView, isTasksTabCollapsed, R.id.tasks_arrow)
        val completedTab = Tab(completedTasksTab, completedRecyclerView, isCompletedTasksTabCollapsed, R.id.tasks_done_arrow)

        val model: ConnectionViewModel by activityViewModels()
        model.getIsConnected().observe(viewLifecycleOwner) {
            if(!it){
                tasksTab.visibility = View.GONE
                completedTasksTab.visibility = View.GONE
                recyclerView.visibility = View.GONE
                completedRecyclerView.visibility = View.GONE

                noConnectionWrapper.visibility = View.VISIBLE
            }
            else{
                tasksTab.visibility = View.VISIBLE
                completedTasksTab.visibility = View.VISIBLE
                if(!tab.isCollapsed){
                    recyclerView.visibility = View.VISIBLE
                }
                if(!completedTab.isCollapsed){
                    completedRecyclerView.visibility = View.VISIBLE
                }

                noConnectionWrapper.visibility = View.GONE
            }

            Log.d("Tasks", "Visibility: ${completedTasksTab.visibility}, View.GONE: ${View.GONE}, View.VISIBLE: ${View.VISIBLE}")
            Log.d("Tasks", it.toString())
        }


        //Collapse the view on click
        tasksTab.setOnClickListener {
            tab.onTabClick()
        }

        //Collapse the view on click
        completedTasksTab.setOnClickListener {
            completedTab.onTabClick()
        }

        //Load data from the web server
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
            adapter.hasDataLoaded = true
            adapter.tasks = taskList
            adapter.notifyDataSetChanged()
        },
            {
                Log.d("TasksFragment", it.toString())
            }
        )

        //If task is marked as done add it to the completedRecyclerView
        val taskClick: PublishSubject<Task> = adapter.taskClickPublisher
        taskClick.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            completedAdapter.tasksCompleted.add(it)
            completedAdapter.notifyItemInserted(completedAdapter.tasksCompleted.size)
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
        private const val ARG_SECTION_NUMBER = "section_number"
        @JvmStatic
        fun newInstance(isConnected: Boolean): TasksFragment {
            return TasksFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SECTION_NUMBER, isConnected)
                }
            }
        }
    }
}