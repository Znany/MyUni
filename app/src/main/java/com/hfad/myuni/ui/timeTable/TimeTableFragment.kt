package com.hfad.myuni.ui.timeTable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.dataClass.Subject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeTableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        backendViewModel.getTimeTable().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe {
            val obj: JSONObject = it.getJSONObject("resultset")
            for (i in 1 until 6){
                val dayObj: JSONArray = obj.getJSONArray(i.toString())

                val subjectList: ArrayList<Subject> = ArrayList()
                for (j in 0 until dayObj.length()){
                    val end: String = dayObj.getJSONObject(j).getString("time_end")
                    val start: String = dayObj.getJSONObject(j).getString("time_start")
                    val name: String = dayObj.getJSONObject(j).getString("name")
                    val type: String = dayObj.getJSONObject(j).getString("lesson_type_name")
                    val room: String = dayObj.getJSONObject(j).getString("room")
                    Log.d("TimeTable", String.format("name: %s, day: %d", name, i))

                    subjectList.add(Subject(String.format("%s (%s %s)", name, type, room), start, end))
                }
                Log.d("TimeTable", "f")
                adapterList[i - 1].lectures = subjectList
                adapterList[i - 1].notifyDataSetChanged()
            }
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeTableFragment.
         */
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