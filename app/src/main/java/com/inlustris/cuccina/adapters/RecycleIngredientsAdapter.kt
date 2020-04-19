package com.inlustris.cuccina.adapters

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.inlustris.cuccina.IngredientsHelper
import com.inlustris.cuccina.R
import com.inlustris.cuccina.beans.Ingredient
import com.inlustris.cuccina.databinding.IngredientCardBinding
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon
import java.util.*

class RecycleIngredientsAdapter(private val activity: Activity, private val ingredientList: ArrayList<Ingredient>) : RecyclerView.Adapter<RecycleIngredientsAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.ingredient_card, parent, false))
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]

        ingredient.quantidade = "${ingredient.quantidade}${ingredient.medidas}"
        holder.ingredientCardBinding.setIngredient(ingredient)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.ingredientCardBinding.quantidade.tooltipText = IngredientsHelper.meaning(ingredient.medidas)
        } else {
            holder.ingredientCardBinding.quantidade.setOnClickListener {
                val balloon = createBalloon(activity) {
                    setArrowSize(10)
                    setWidthRatio(1.0f)
                    setHeight(65)
                    setArrowPosition(0.7f)
                    setCornerRadius(4f)
                    setAlpha(0.9f)
                    setText(IngredientsHelper.meaning(ingredient.medidas))
                    setBalloonAnimation(BalloonAnimation.FADE)
                }
                balloon.show(it)
            }

        }
    }


    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class IngredientViewHolder(val ingredientCardBinding: IngredientCardBinding) : RecyclerView.ViewHolder(ingredientCardBinding.root)


}