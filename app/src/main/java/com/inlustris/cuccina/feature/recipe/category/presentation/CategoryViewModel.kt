package com.inlustris.cuccina.feature.recipe.category.presentation

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.inlustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(application: Application, override val service: RecipeService) :
    BaseViewModel<Recipe>(application) {


    fun getCategoryRecipes(category: String) = viewModelScope.launch(Dispatchers.IO) {
        updateViewState(ViewModelBaseState.LoadingState)
        when(val categoryQuery = service.getRecipesByCategory(category)) {
            is ServiceResult.Error -> {
                updateViewState(ViewModelBaseState.ErrorState(categoryQuery.errorException))
            }
            is ServiceResult.Success -> {
                delay(2000)
                updateViewState(ViewModelBaseState.DataListRetrievedState(categoryQuery.data))
            }
        }
    }

}