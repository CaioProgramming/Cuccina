package com.inlustris.cuccina.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.inlustris.cuccina.IngredientsHelper
import com.inlustris.cuccina.R
import com.inlustris.cuccina.beans.Ingredient
import com.inlustris.cuccina.databinding.IngredientCardBinding
import java.util.*

class RecycleIngredientsAdapter(private val activity: Activity, private val ingredientList: ArrayList<Ingredient>) : RecyclerView.Adapter<RecycleIngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.ingredient_card, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.ingredientCardBinding.setIngrediente(ingredient)
        holder.ingredientCardBinding.ingredientemoji.text = findAnEmoji(ingredient)
    }

    private fun findAnEmoji(ingredient: Ingredient): String? {
        for ((k, v) in IngredientsHelper.emojis) {
            if (k.contains(ingredient.ingrediente!!)) {
                return IngredientsHelper.emojis.getValue(v)
            }
        }
        return IngredientsHelper.emojis[IngredientsHelper.emojis.entries.shuffled().first()]
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class IngredientViewHolder(val ingredientCardBinding: IngredientCardBinding) : RecyclerView.ViewHolder(ingredientCardBinding.root)


}