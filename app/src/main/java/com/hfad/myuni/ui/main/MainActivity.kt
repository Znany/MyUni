package com.hfad.myuni.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.hfad.myuni.R
import com.hfad.myuni.ui.addTask.AddTaskActivity
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_refresh)
        val backendViewModel = BackEndViewModel(application)
        val model: ConnectionViewModel by viewModels()

        fab.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }

        //This enables the viewPager to work with the swipeRefreshLayout
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {
                swipeRefreshLayout.isEnabled = (state == ViewPager.SCROLL_STATE_IDLE)
            }
        })



        fun checkConnection() {
            backendViewModel.getInternetConnection().observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe ({
                Log.d("Main", "IsConnected: $it")
                model.isConnected(it)
                swipeRefreshLayout.isRefreshing = false
            },
                {
                    Log.d("Main", it.toString())
                    model.isConnected(false)
                    swipeRefreshLayout.isRefreshing = false
                })
        }

        checkConnection()

        swipeRefreshLayout.setOnRefreshListener{
            checkConnection()
        }

    }
}