package com.silent.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silent.core.data.Recipe
import com.silent.core.data.RecipeGroup
import com.silent.core.databinding.RecipeGroupLayoutBinding

class RecipeGroupAdapter(
    val recipeGroups: List<RecipeGroup>,
    val onSelectRecipe: (Recipe) -> Unit
) :
    RecyclerView.Adapter<RecipeGroupAdapter.RecipeGroupViewHolder>() {

    inner class RecipeGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = RecipeGroupLayoutBinding.bind(itemView)

        fun bindData() {
            recipeGroups[bindingAdapterPosition].run {
                binding.categoryTitle.text = title
                binding.recipesRecycler.adapter =
                    RecipeAdapter(recipes, onSelectRecipe = onSelectRecipe)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeGroupViewHolder {
        return RecipeGroupViewHolder(
            RecipeGroupLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: RecipeGroupViewHolder, position: Int) {
        holder.bindData()
    }

    override fun getItemCount() = recipeGroups.size

}