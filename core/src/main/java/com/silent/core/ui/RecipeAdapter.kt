package com.silent.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.silent.core.data.Recipe
import com.silent.core.databinding.RecipeViewBinding
import com.silent.core.ui.RecipeAdapter.RecipeViewHolder

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onSelectRecipe: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecipeViewBinding.bind(itemView)

        fun bind() {
            recipes[bindingAdapterPosition].run {
                binding.recipeName.text = name
                binding.recipeDescription.text = description
                Glide.with(itemView.context).load(image).into(binding.recipePic)
                binding.card.setOnClickListener {
                    onSelectRecipe(this)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            RecipeViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = recipes.size
}