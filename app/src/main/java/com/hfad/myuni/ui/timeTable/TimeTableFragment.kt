package com.hfad.myuni.ui.timeTable

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.dataClass.Subject
import com.hfad.myuni.ui.localDatabase.DatabaseBuilder
import com.hfad.myuni.ui.main.ConnectionViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TimeTableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_time_table, container, false)

        val recyclerViewPon: RecyclerView = root.findViewById(R.id.time_table_recycler_pon)
        val recyclerViewWt: RecyclerView = root.findViewById(R.id.time_table_recycler_wt)
        val recyclerViewSr: RecyclerView = root.findViewById(R.id.time_table_recycler_sr)
        val recyclerViewCzw: RecyclerView = root.findViewById(R.id.time_table_recycler_czw)
        val recyclerViewPt: RecyclerView = root.findViewById(R.id.time_table_recycler_pt)

        val listRecycler = mutableListOf(recyclerViewPon, recyclerViewWt, recyclerViewSr, recyclerViewCzw, recyclerViewPt)

        val layoutManagerPon = LinearLayoutManager(context)
        recyclerViewPon.layoutManager = layoutManagerPon
        val layoutManagerWt = LinearLayoutManager(context)
        recyclerViewWt.layoutManager = layoutManagerWt
        val layoutManagerSr = LinearLayoutManager(context)
        recyclerViewSr.layoutManager = layoutManagerSr
        val layoutManagerCzw = LinearLayoutManager(context)
        recyclerViewCzw.layoutManager = layoutManagerCzw
        val layoutManagerPt = LinearLayoutManager(context)
        recyclerViewPt.layoutManager = layoutManagerPt

        val adapterPon = TimeTableAdapter()
        recyclerViewPon.adapter = adapterPon
        val adapterWt = TimeTableAdapter()
        recyclerViewWt.adapter = adapterWt
        val adapterSr = TimeTableAdapter()
        recyclerViewSr.adapter = adapterSr
        val adapterCzw = TimeTableAdapter()
        recyclerViewCzw.adapter = adapterCzw
        val adapterPt = TimeTableAdapter()
        recyclerViewPt.adapter = adapterPt

        val adapterList = mutableListOf(adapterPon, adapterWt, adapterSr, adapterCzw, adapterPt)

        val backendViewModel = BackEndViewModel(requireActivity().application)

        val localDbBuilder = DatabaseBuilder()
        val localDB = localDbBuilder.getDb(requireContext())


        //Load TimeTable from the web server
        fun loadData() {

            backendViewModel.getTimeTable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe ({
                val obj: JSONObject = it.getJSONObject("resultset")
                val allSubjects: ArrayList<Subject> = ArrayList()
                for (i in 1 until 6){
                    val dayObj: JSONArray = obj.getJSONArray(i.toString())

                    val subjectList: ArrayList<Subject> = ArrayList()
                    for (j in 0 until dayObj.length()){
                        val end: String = dayObj.getJSONObject(j).getString("time_end")
                        val start: String = dayObj.getJSONObject(j).getString("time_start")
                        val name: String = dayObj.getJSONObject(j).getString("name")
                        val type: String = dayObj.getJSONObject(j).getString("lesson_type_name")
                        var room: String = dayObj.getJSONObject(j).getString("room")

                        if (room == "0"){
                            room = "Online"
                        }

                        allSubjects.add(Subject((i*10)+j,"$name ($type $room)", start, end, i))
                        subjectList.add(Subject((i*10)+j,"$name ($type $room)", start, end, i))
                    }
                    adapterList[i - 1].notifyItemRangeRemoved(0, adapterList[i - 1].lectures.size)
                    adapterList[i - 1].lectures = subjectList
                    adapterList[i - 1].notifyItemRangeInserted(0, subjectList.size - 1)
                }

                //Scroll to current day on the screen
                val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                if (currentDay in 2..5){
                    root.post {
                        //root.scrollTo(0, (listRecycler[currentDay - 1].top + listRecycler[currentDay - 1].bottom - root.height) / 2)
                        val rect: Rect = Rect(0, 0, listRecycler[currentDay - 1].width, listRecycler[currentDay - 1].height)
                        listRecycler[currentDay - 1].requestRectangleOnScreen(rect, true)
                    }
                }

                //Add subjects to local database
                runBlocking {
                    launch {
                        localDB.databaseDao().clearAllSubjects()
                        localDB.databaseDao().insertSubjects(allSubjects)
                    }
                }

            },
                {
                    Log.d("TimeTable", it.toString())

                }
            )

        }

        //On swipeRefreshLayout
        val model: ConnectionViewModel by activityViewModels()
        model.getIsConnected().observe(viewLifecycleOwner){ isConnected ->
            if(!isConnected){

                runBlocking {
                    launch {
                        val subjects = localDB.databaseDao().getSubjects()

                        for (adapter in adapterList){
                            adapter.notifyItemRangeRemoved(0, adapter.lectures.size);
                            adapter.lectures.clear()
                        }

                        for (subject in subjects){
                            adapterList[subject.day - 1].lectures.add(subject)
                            adapterList[subject.day - 1].notifyItemInserted(adapterList[subject.day - 1].lectures.size - 1)
                        }

                        if (subjects.isEmpty()){
                            for (recycler in listRecycler){
                                recycler.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            else{

                loadData()
                for (recycler in listRecycler){
                    recycler.visibility = View.VISIBLE
                }
            }

        }

        return root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeTableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}