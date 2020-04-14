package com.inlustris.cuccina.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.inlustris.cuccina.R
import com.inlustris.cuccina.beans.Step
import com.inlustris.cuccina.databinding.StepLayoutBinding
import java.util.*

class RecyclerStepsAdapter(private val activity: Activity, private val mData: ArrayList<Step>) : RecyclerView.Adapter<RecyclerStepsAdapter.StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.step_layout, parent, false))
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = mData[position]
        step.count = "${step.count!!.toInt() + 1}"
        holder.stepLayoutBinding.passo = step
        val animation = AnimationUtils.loadAnimation(activity, R.anim.fui_slide_in_right)
        holder.stepLayoutBinding.card.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class StepViewHolder(val stepLayoutBinding: StepLayoutBinding) : RecyclerView.ViewHolder(stepLayoutBinding.root)


}