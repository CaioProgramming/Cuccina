package com.inlustris.cuccina.adapters

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Handler
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.inlustris.cuccina.R
import com.inlustris.cuccina.StartrecipeActivity
import com.inlustris.cuccina.beans.Recipe
import com.inlustris.cuccina.databinding.BigRecipeLayoutBinding
import com.inlustris.cuccina.databinding.LastRecipeLayoutBinding
import com.inlustris.cuccina.databinding.RecipeLayoutBinding
import com.mikhaellopez.rxanimation.fadeIn
import java.util.*

class RecyclerAdapter(private val activity: Activity, var recipes: ArrayList<Recipe>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val RECIPE = 1
    private val LAST = 2
    private val BIGRECIPE = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RECIPE -> RecipeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.recipe_layout, parent, false))
            BIGRECIPE -> BigRecipeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.big_recipe_layout, parent, false))
            else -> LastRecipeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.last_recipe_layout, parent, false))
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (recipes != null && recipes!!.size > 0) {
            val recipe = recipes!![position]
            if (recipe.isLastRecipe()) {
                return LAST
            }
            if (position == 0) {
                return BIGRECIPE
            }
            return RECIPE
        }
        return RECIPE
    }


    override fun getItemCount(): Int {
        return if (recipes.isNullOrEmpty()) {
            4
        } else {
            recipes!!.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == RECIPE) {
            val recipeViewHolder: RecipeViewHolder = holder as RecipeViewHolder
            if (recipes != null && recipes!!.size > 0) {
                val recipe = recipes!![position]
                recipeViewHolder.recipeBinding.recipe = recipe
                loadPic(holder.recipeBinding.pic, recipe.imageurl)
                recipeViewHolder.recipeBinding.mainshimmer.fadeIn().doOnComplete {
                    stopShimmer(holder.recipeBinding.mainshimmer)
                }.subscribe()
                recipeViewHolder.recipeBinding.card.setOnClickListener { startRecipe(recipe, holder.recipeBinding.receita) }
            } else {
                recipeViewHolder.recipeBinding.mainshimmer.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom_repeat))
            }
        } else if (getItemViewType(position) == BIGRECIPE) {
            val bigrecipeViewHolder: BigRecipeViewHolder = holder as BigRecipeViewHolder
            if (recipes != null && recipes!!.size > 0) {
                val recipe = recipes!![position]
                bigrecipeViewHolder.bigRecipeLayoutBinding.recipe = recipe
                loadPic(holder.bigRecipeLayoutBinding.pic, recipe.imageurl)
                bigrecipeViewHolder.bigRecipeLayoutBinding.mainshimmer.fadeIn().doOnComplete {
                    stopShimmer(holder.bigRecipeLayoutBinding.mainshimmer)
                }.subscribe()
                bigrecipeViewHolder.bigRecipeLayoutBinding.card.setOnClickListener { startRecipe(recipe, holder.bigRecipeLayoutBinding.receita) }
            } else {
                bigrecipeViewHolder.bigRecipeLayoutBinding.mainshimmer.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom_repeat))
            }
        } else {
            val cookers = arrayOf("👨‍🍳", "👩‍🍳")
            val chief = cookers[Random().nextInt(cookers.size)]
            val lastrecipeViewHolder = holder as LastRecipeViewHolder
            lastrecipeViewHolder.lastRecipeLayoutBinding.cookemoji.text = chief
        }
    }


    private fun loadPic(imageView: ImageView, url: String?) {
        Glide.with(activity).load(url).error(R.drawable.picnotfound).into(imageView)

    }

    private fun stopShimmer(shimmerFrameLayout: ShimmerFrameLayout) {
        Handler().postDelayed({ shimmerFrameLayout.hideShimmer() }, 2000)
    }

    private fun startRecipe(recipe: Recipe, view: View) {
        val i = Intent(activity, StartrecipeActivity::class.java)
        i.putExtra("Recipe", recipe)
        val p2: Pair<View, String> = Pair.create(view, "RecipeName")
        val options = ActivityOptions.makeSceneTransitionAnimation(activity, p2)
        activity.startActivity(i, options.toBundle())
    }


    class RecipeViewHolder(val recipeBinding: RecipeLayoutBinding) : RecyclerView.ViewHolder(recipeBinding.root)
    class BigRecipeViewHolder(val bigRecipeLayoutBinding: BigRecipeLayoutBinding) : RecyclerView.ViewHolder(bigRecipeLayoutBinding.root)
    class LastRecipeViewHolder(val lastRecipeLayoutBinding: LastRecipeLayoutBinding) : RecyclerView.ViewHolder(lastRecipeLayoutBinding.root)


}