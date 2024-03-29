package com.inlustris.cuccina.feature.home.presentation

import ai.atick.material.MaterialColor
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inlustris.cuccina.feature.recipe.category.domain.model.Category
import com.inlustris.cuccina.feature.recipe.domain.model.Recipe
import com.inlustris.cuccina.feature.recipe.domain.model.RecipeGroup
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.ui.theme.Page
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {


    val homeList = MutableLiveData<List<RecipeGroup>>()

    val highlightRecipes = MutableLiveData<List<Page>>()


    fun loadHome() {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            val data = service.getAllData()
            if (data.isSuccess) {
                val recipes = data.success.data as List<Recipe>
                getHighlights(recipes.sortedBy { it.likes.size }.take(recipes.size / 2))
                groupRecipes(recipes)
            } else {
                updateViewState(ViewModelBaseState.ErrorState(data.error.errorException))
            }
            delay(1000)
            updateViewState(ViewModelBaseState.LoadCompleteState)
        }
    }

    private fun groupRecipes(recipes: List<Recipe>) {
        val categoriesRecipes = recipes.groupBy { it.category }.map {
            val category = Category.values().find { category -> category.name == it.key } ?: Category.UNKNOWN
            RecipeGroup(category.title, it.value)
        }.sortedBy { it.title }
        homeList.postValue(categoriesRecipes)
    }

    private fun getHighlights(recipes: List<Recipe>) {
        val pages = mutableListOf<Page>()
        val emojisBackgrounds = ArrayList<String>()
        repeat(3) {
            emojisBackgrounds.add(IngredientMapper.emojiList().random())
        }
        pages.add(
            Page.AnimatedTextPage(
                "Tem novidade na cozinha!",
                "Novas receitas foram adicionadas ao app, confira!",
                emojisBackgrounds.filter { it != "❓" },
                backColor = MaterialColor.OrangeA100,
                textColor = MaterialColor.White
            )
        )
        recipes.forEach {
            pages.add(Page.HighlightPage(it.name, it.description, it.photo, it.id))
        }

        highlightRecipes.postValue(pages)
    }

    fun searchRecipe(query: String) {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            val data = service.getAllData()
            if (data.isSuccess) {
                val recipes = (data.success.data as List<Recipe>).filter {
                    it.name.contains(query, true) || it.description.contains(
                        query,
                        true
                    ) || it.ingredients.any { ingredient ->
                        ingredient.name.contains(query, true)
                    }
                }
                groupRecipes(recipes)
            } else {
                updateViewState(ViewModelBaseState.ErrorState(data.error.errorException))
            }
            updateViewState(ViewModelBaseState.LoadCompleteState)
        }
    }


}