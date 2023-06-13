package com.inlustris.cuccina.feature.recipe.form.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.inlustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.inlustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.ilustris.cuccina.ui.theme.FormPage
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {

    val recipe = MutableLiveData(Recipe())
    val pages = MutableLiveData<ArrayList<FormPage>>(arrayListOf())
    val currentPage = MutableLiveData(0)
    val requireValidateEmail = MutableLiveData(false)

    private fun clearRecipe() {
        recipe.postValue(Recipe())
        pages.postValue(arrayListOf())
    }

    private fun updateRecipeName(name: String) {
        recipe.postValue(recipe.value?.copy(name = name))
        updatePage(FormPage.DescriptionFormPage {
            updateRecipeDescription(it)
        })
    }

    private fun updateRecipeDescription(description: String) {
        recipe.postValue(recipe.value?.copy(description = description))
        updatePage(FormPage.ImageFormPage {
            updateRecipePhoto(it)
        })
    }

    private fun updateRecipeTime(time: Long) {
        recipe.postValue(recipe.value?.copy(time = time))
        updatePage(FormPage.PortionsFormPage {
            updateRecipePortions(it)
        })
    }

    private fun updateRecipePortions(portions: Int) {
        recipe.postValue(recipe.value?.copy(portions = portions))
        updatePage(FormPage.CaloriesFormPage {
            updateCalories(it)
        })
    }

    fun updateRecipeIngredients(ingredient: Ingredient) {
        Log.i(javaClass.simpleName, "updateRecipeIngredients: adding ingredient -> $ingredient")
        recipe.postValue(
            recipe.value?.copy(
                ingredients = recipe.value?.ingredients?.plus(ingredient) ?: listOf(ingredient)
            )
        )
    }

    private fun updateRecipeIngredients(ingredient: List<Ingredient>) {
        Log.i(javaClass.simpleName, "updateRecipeIngredients: adding ingredient -> $ingredient")
        recipe.postValue(
            recipe.value?.copy(ingredients = ingredient)
        )
        updatePage(FormPage.StepsFormPage(ingredient) {
            updateRecipeSteps(it)
        })
    }

    fun removeRecipeIngredient(ingredient: Ingredient) {
        recipe.postValue(
            recipe.value?.copy(
                ingredients = recipe.value?.ingredients?.minus(
                    ingredient
                ) ?: listOf(ingredient)
            )
        )
    }

    private fun updateRecipePhoto(photo: String) {
        recipe.postValue(recipe.value?.copy(photo = photo))
        updatePage(FormPage.TimeFormPage {
            updateRecipeTime(it)
        })
    }

    fun updateRecipeSteps(step: Step) {
        Log.i(javaClass.simpleName, "updateRecipeSteps: adding step -> $step")
        recipe.postValue(
            recipe.value?.copy(
                steps = recipe.value?.steps?.plus(step) ?: listOf(step)
            )
        )
    }

    private fun updateRecipeSteps(step: List<Step>) {
        Log.i(javaClass.simpleName, "updateRecipeSteps: adding step -> $step")
        recipe.value = recipe.value?.copy(steps = step)
        recipe.value?.let {
            saveData(it.apply {
                publishDate = Calendar.getInstance().timeInMillis
                steps = step
            })
        }
    }

    fun updateRecipeStep(step: Step) {
        recipe.postValue(
            recipe.value?.copy(
                steps = recipe.value?.steps?.minus(step)?.plus(step) ?: listOf(step)
            )
        )
    }

    private fun updateRecipeCategory(category: String) {
        recipe.postValue(recipe.value?.copy(category = category))
        updatePage(FormPage.NameFormPage {
            updateRecipeName(it)
        })
    }

    private fun updateCalories(calories: Int) {
        recipe.postValue(recipe.value?.copy(calories = calories))
        updatePage(FormPage.IngredientsFormPage {
            updateRecipeIngredients(it)
        })
    }

    fun updatePortions(portions: Int) {
        recipe.postValue(recipe.value?.copy(portions = portions))
    }

    private fun updatePage(page: FormPage) {
        pages.value?.apply {
            if (none { it::class == page::class }) {
                add(page)
                pages.postValue(this)
            }
        } ?: run {
            pages.postValue(arrayListOf(page))
        }
        currentPage.postValue(pages.value?.size ?: 0)
    }

    fun buildFirstPage() {
        getUser()?.let {
            if (it.isEmailVerified) {
                updatePage(FormPage.CategoryFormPage {
                    updateRecipeCategory(it)
                })
            } else {
                requireValidateEmail.postValue(true)
            }
        }
    }

    fun pushValidationEmail() {
        getUser()?.sendEmailVerification()
    }
}