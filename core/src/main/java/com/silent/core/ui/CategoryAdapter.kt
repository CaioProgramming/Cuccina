package com.silent.core.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.silent.core.R
import com.silent.core.data.Category
import com.silent.core.databinding.IngredientCardLayoutBinding

class CategoryAdapter(val categories: List<Category>, val onCategorySelect: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = IngredientCardLayoutBinding.bind(itemView)

        fun bind() {
            categories[bindingAdapterPosition].run {
                binding.ingredientCard.text = icon
                binding.ingredientCard.setOnClickListener {
                    onCategorySelect(this)
                }
                binding.ingredientCard.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor(this.color))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredient_card_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = categories.size

}