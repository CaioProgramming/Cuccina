package com.inlustris.cuccina.feature.recipe.category.presentation

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.silent.ilustriscore.core.model.BaseService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ErrorType
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(application: Application, override val service: RecipeService) :
    BaseViewModel<Recipe>(application) {


    fun getCategoryRecipes(category: String) = viewModelScope.launch(Dispatchers.IO) {
        updateViewState(ViewModelBaseState.LoadingState)
        val categoryQuery = service.getRecipesByCategory(category)
        when(categoryQuery) {
            is ServiceResult.Error -> {
                updateViewState(ViewModelBaseState.ErrorState(categoryQuery.errorException))
            }
            is ServiceResult.Success -> {
                updateViewState(ViewModelBaseState.DataListRetrievedState(categoryQuery.data))
            }
        }
    }

}