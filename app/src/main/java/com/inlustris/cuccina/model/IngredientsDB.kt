package com.inlustris.cuccina.model

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.inlustris.cuccina.IngredientsHelper
import com.inlustris.cuccina.beans.Ingredient
import com.inlustris.cuccina.beans.Recipe

class IngredientsDB(val recipe: Recipe, activity: Activity) : ModelBase(activity) {
    override var path: DatabaseReference = reference.getReference("recipes").child(recipe.id!!).child("ingredientes")
    var ingredientsListener: ModelListener.IngredientsListener = object : ModelListener.IngredientsListener {
        override fun ingredientsLoaded(ingredients: ArrayList<Ingredient>) {
            success("Carregou ${ingredients.size} resultados de ingredientes")
        }
    }


    fun carregar(ingredientsListener: ModelListener.IngredientsListener) {
        this.ingredientsListener = ingredientsListener
        path.addValueEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError) {
        error(p0.message)
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val ingredientArrayList: ArrayList<Ingredient> = ArrayList()
        for (d in dataSnapshot.children) {
            val i = d.getValue(Ingredient::class.java)
            i?.let {
                it.count = d.key
                if (it.quantidade == "0.5") {
                    it.quantidade = "1/2"
                }
                it.medidas = IngredientsHelper.abreviate(it.medidas?.toLowerCase())
                ingredientArrayList.add(it)
            }
        }
        Log.i(javaClass.simpleName, "Loaded ${ingredientArrayList.size} ingredients")

        ingredientsListener.ingredientsLoaded(ingredientArrayList)
    }
}