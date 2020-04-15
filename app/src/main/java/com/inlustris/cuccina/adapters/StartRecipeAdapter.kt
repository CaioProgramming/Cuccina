package com.inlustris.cuccina.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.inlustris.cuccina.R
import com.inlustris.cuccina.beans.Step
import com.inlustris.cuccina.databinding.StepLayoutBinding

class StartRecipeAdapter(val activity: Activity, val stepList: ArrayList<Step>) : PagerAdapter() {
    override fun getCount(): Int {
        return stepList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val step = stepList[position]
        val stepLayoutBinding = DataBindingUtil.inflate<StepLayoutBinding>(LayoutInflater.from(activity), R.layout.step_layout, container, false)
        stepLayoutBinding.passo = step
        val animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom)
        stepLayoutBinding.card.startAnimation(animation)
        stepLayoutBinding.card.visibility = View.VISIBLE
        container.addView(stepLayoutBinding.root)
        return stepLayoutBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeViewInLayout(`object` as LinearLayout)
    }


}