package com.inlustris.cuccina.model

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.inlustris.cuccina.beans.Recipe
import com.inlustris.cuccina.beans.Step

class StepsDB(val recipe: Recipe, activity: Activity) : ModelBase(activity) {
    override var path: DatabaseReference = reference.getReference("recipes").child(recipe.id!!).child("passos")
    var stepsListener: ModelListener.StepsListener = object : ModelListener.StepsListener {
        override fun stepsLoaded(steps: ArrayList<Step>) {
            success("Carregou ${steps.size} passos")
        }
    }


    fun carregar(stepsListener: ModelListener.StepsListener) {
        this.stepsListener = stepsListener
        path.addValueEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
        error(p0.message)
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val stepslist: ArrayList<Step> = ArrayList()
        for (d in dataSnapshot.children) {
            val s = d.getValue(Step::class.java)
            s?.let {
                it.count = d.key
                stepslist.add(it)
            }
        }
        Log.i(javaClass.simpleName, "Loaded ${stepslist.size} steps")
        stepsListener.stepsLoaded(stepslist)
    }
}