package com.hfad.myuni.ui.tasks

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.hfad.myuni.R

class Tab(var tab: ConstraintLayout, var recyclerView: RecyclerView, var isCollapsed: Boolean, var taskArrowId: Int) {
    fun onTabClick(){
        if(isCollapsed){
            isCollapsed = false
            tab.findViewById<ImageView>(taskArrowId).setImageResource(R.drawable.arrow_facing_down)
            recyclerView.apply {
                alpha = 0f
                visibility = View.VISIBLE
                animate().alpha(1f).setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).setListener(null)
            }
        }
        else{
            isCollapsed = true
            tab.findViewById<ImageView>(taskArrowId).setImageResource(R.drawable.arrow_facing_up)
            recyclerView.apply {
                alpha = 1f
                animate().alpha(0f).setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong()).setListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator) {
                        recyclerView.visibility = View.GONE
                    }

                })
            }
        }
    }
}