package com.inlustris.cuccina.model

import com.inlustris.cuccina.beans.Ingredient
import com.inlustris.cuccina.beans.Recipe
import com.inlustris.cuccina.beans.Step

interface ModelListener {
    interface RecipesListener {
        fun recipesLoaded(recipes: ArrayList<Recipe>)
    }

    interface IngredientsListener {
        fun ingredientsLoaded(ingredients: ArrayList<Ingredient>)
    }

    interface StepsListener {
        fun stepsLoaded(steps: ArrayList<Step>)
    }
}