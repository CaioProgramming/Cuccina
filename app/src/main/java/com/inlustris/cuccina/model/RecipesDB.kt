package com.inlustris.cuccina.model

import android.app.Activity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.inlustris.cuccina.beans.Recipe

class RecipesDB(activity: Activity) : ModelBase(activity), ValueEventListener {

    var recipesloaded = object : ModelListener.RecipesListener {
        override fun recipesLoaded(recipes: ArrayList<Recipe>) {
            success("Carregou ${recipes.size} receitas")
        }
    }

    override var path = reference.getReference("recipes")

    fun carregar(recipesListener: ModelListener.RecipesListener?, query: String?) {
        recipesListener?.let { recipesloaded = it }
        if (query.isNullOrEmpty() || query.isNullOrBlank() || query.equals("Home", true)) {
            path.orderByChild("tipo").addValueEventListener(this)
        } else {
            path.orderByChild("tipo").equalTo(query).addValueEventListener(this)
        }
    }

    fun inserir(recipe: Recipe) {
        path.push().setValue(recipe, insertListener)
    }

    fun remover(id: String) {
        path.child(id).removeValue(removedListener)
    }

    fun alterar(recipe: Recipe) {
        path.child(recipe.id!!).setValue(recipe, updateListener)
    }

    override fun onCancelled(error: DatabaseError) {
        error(error.message)
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val recipeArrayList = ArrayList<Recipe>()
        for (d in dataSnapshot.children) {
            val r: Recipe? = d.getValue(Recipe::class.java)
            r?.let {
                it.id = d.key
                recipeArrayList.add(it)
            }
        }
        recipeArrayList.add(Recipe.lastRecipe())
        Log.i(javaClass.simpleName, "Loaded ${recipeArrayList.size - 1} recipes")

        recipesloaded.recipesLoaded(recipeArrayList)
    }


}