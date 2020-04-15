package com.inlustris.cuccina

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.bumptech.glide.Glide
import com.inlustris.cuccina.adapters.RecycleIngredientsAdapter

import com.inlustris.cuccina.adapters.StartRecipeAdapter
import com.inlustris.cuccina.beans.Ingredient
import com.inlustris.cuccina.beans.Recipe
import com.inlustris.cuccina.beans.Step

import com.inlustris.cuccina.databinding.ActivityStartrecipeBinding
import com.inlustris.cuccina.model.IngredientsDB
import com.inlustris.cuccina.model.ModelListener
import com.inlustris.cuccina.model.StepsDB
import de.mateware.snacky.Snacky
import kotlinx.android.synthetic.main.activity_startrecipe.*

class StartrecipeActivity : AppCompatActivity() {
    private var startrecipeBinding: ActivityStartrecipeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startrecipeBinding = DataBindingUtil.setContentView(this, R.layout.activity_startrecipe)
        setContentView(startrecipeBinding!!.root)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        loadRecipe()
    }


    private fun loadRecipe() {
        val recipe: Recipe = intent.getSerializableExtra("Recipe") as Recipe
        recipe.calorias = "🔥\n${recipe.calorias} kcal"
        recipe.tempo = "⏰\n${recipe.tempo}"
        startrecipeBinding?.recipe = recipe
        Glide.with(this).load(recipe.imageurl).into(foodpick)
        loadSteps(recipe)
        loadIngredients(recipe)
    }

    private fun setupTabs(size: Int) {
        for (i in 0..size) {
            tabs.getTabAt(i)?.setIcon(R.drawable.dot)
        }
    }

    private fun loadIngredients(recipe: Recipe) {
        IngredientsDB(recipe, this).carregar(object : ModelListener.IngredientsListener {
            override fun ingredientsLoaded(ingredients: ArrayList<Ingredient>) {
                if (ingredients.size > 0) {
                    startrecipeBinding?.ingredientsSheet?.ingredientsRecycler?.adapter = RecycleIngredientsAdapter(this@StartrecipeActivity, ingredients)
                    startrecipeBinding?.ingredientsSheet?.ingredientsRecycler?.layoutManager = GridLayoutManager(this@StartrecipeActivity, 1, VERTICAL, false)
                } else {
                    Snacky.builder().setActivity(this@StartrecipeActivity).setText("Erro ao obter ingredientes da receita").build().show()
                }

            }
        })
    }


    private fun loadSteps(recipe: Recipe) {
        StepsDB(recipe, this).carregar(object : ModelListener.StepsListener {
            override fun stepsLoaded(steps: ArrayList<Step>) {
                if (steps.size > 0) {
                    val adapter = StartRecipeAdapter(this@StartrecipeActivity, steps)
                    items.adapter = adapter
                    tabs.setupWithViewPager(items)
                    setupTabs(steps.size)
                } else {
                    Snacky.builder().setActivity(this@StartrecipeActivity).setText("Erro ao obter passo a passo da receita").error().show()
                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) { // app icon in action bar clicked; go home
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item!!)
    }
}