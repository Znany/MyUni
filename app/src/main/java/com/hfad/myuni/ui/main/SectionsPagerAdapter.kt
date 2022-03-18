package com.hfad.myuni.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hfad.myuni.R
import com.hfad.myuni.ui.tasks.TasksFragment
import com.hfad.myuni.ui.timeTable.TimeTableFragment

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    var isConnected = false

    override fun getItem(position: Int): Fragment {
        return if(position == 0){
            TasksFragment.newInstance(isConnected)
        } else{
            TimeTableFragment.newInstance("test", "test")
        }

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}