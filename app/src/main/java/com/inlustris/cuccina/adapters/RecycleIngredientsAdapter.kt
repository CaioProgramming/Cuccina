package com.inlustris.cuccina.adapters

import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
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
        holder.ingredientCardBinding.ingrediente.text = Html.fromHtml("<b>${ingredient.quantidade} ${ingredient.medidas}</b> ${ingredient.ingrediente}")

    }


    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class IngredientViewHolder(val ingredientCardBinding: IngredientCardBinding) : RecyclerView.ViewHolder(ingredientCardBinding.root)


}