<<<<<<< HEAD:app/src/main/java/com/hfad/myuni/ui/main/MainActivity.kt
package com.hfad.myuni.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.hfad.myuni.AddTaskActivity
import com.hfad.myuni.R

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

        fab.setOnClickListener { view ->
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }
=======
package com.hfad.myuni

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
import com.hfad.myuni.ui.addTask.AddTaskActivity
import com.hfad.myuni.ui.backEnd.BackEndViewModel
import com.hfad.myuni.ui.main.ConnectionViewModel
import com.hfad.myuni.ui.main.SectionsPagerAdapter
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
>>>>>>> e082834ab7b28ae059bd643753223e2bcc5c134b:app/src/main/java/com/hfad/myuni/MainActivity.kt
}