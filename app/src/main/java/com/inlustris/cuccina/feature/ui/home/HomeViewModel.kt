package com.inlustris.cuccina.feature.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.silent.core.data.Category
import com.silent.core.data.Recipe
import com.silent.core.data.RecipeGroup
import com.silent.core.service.CategoriesService
import com.silent.core.service.RecipesService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel<Recipe>(application) {

    sealed class HomeState {
        class HomeGroupsRetrieved(val groups: List<RecipeGroup>) : HomeState()
        class CategoriesRetrieved(val categories: List<Category>) : HomeState()
    }

    override val service = RecipesService()
    val categoryService = CategoriesService()
    val homeState = MutableLiveData<HomeState>()

    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = service.getAllData()
            when (request) {
                is ServiceResult.Error -> {
                    viewModelState.postValue(ViewModelBaseState.ErrorState(request.errorException))
                }
                is ServiceResult.Success -> {
                    getGroups(request.data as ArrayList<Recipe>)
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val request = categoryService.getAllData()
            when (request) {
                is ServiceResult.Error -> {
                    Log.e(
                        this@HomeViewModel::javaClass.name,
                        "getCategories error ->  ${request.errorException}"
                    )
                }
                is ServiceResult.Success -> {
                    homeState.postValue(HomeState.CategoriesRetrieved(request.data as ArrayList<Category>))
                }
            }
        }

    }


    private fun getGroups(recipes: ArrayList<Recipe>) {
        val group = RecipeGroup("Últimas receitas", recipes)
        homeState.postValue(HomeState.HomeGroupsRetrieved(listOf(group)))
    }


}