@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class
)

package com.inlustris.cuccina.feature.recipe.form.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.inlustris.cuccina.feature.home.ui.HOME_ROUTE
import com.inlustris.cuccina.feature.recipe.form.presentation.viewmodel.NewRecipeViewModel
import com.inlustris.cuccina.theme.GetStateComponent
import com.ilustris.cuccina.ui.theme.getFormView
import com.inlustris.cuccina.theme.StateComponent
import com.silent.ilustriscore.core.model.ViewModelBaseState

const val NEW_RECIPE_ROUTE = "new_recipe"

@Composable
fun NewRecipeView(newRecipeViewModel: NewRecipeViewModel, navController: NavController) {

    val recipe = newRecipeViewModel.recipe.observeAsState().value
    val pages = newRecipeViewModel.pages.observeAsState()
    val currentPage = newRecipeViewModel.currentPage.observeAsState()
    val baseState = newRecipeViewModel.viewModelState.observeAsState().value
    val requireEmailValidation = newRecipeViewModel.requireValidateEmail.observeAsState().value
    val pagerState = rememberPagerState()
    val keyboardController = LocalSoftwareKeyboardController.current


    Log.i("NewRecipeView", "NewRecipeView: current recipe $recipe")
    Log.i("NewRecipeView", "current pages -> ${pages.value?.size}")
    Log.i("NewRecipeView", "current page -> ${currentPage.value}")
    LaunchedEffect(currentPage.value) {
        pagerState.animateScrollToPage(currentPage.value ?: 0)
        keyboardController?.hide()
    }

    val showPages =
        pages.value?.isNotEmpty() == true && baseState != ViewModelBaseState.LoadingState && baseState !is ViewModelBaseState.DataSavedState

    AnimatedVisibility(visible = showPages, enter = fadeIn(), exit = fadeOut()) {
        val pageList = pages.value!!
        HorizontalPager(pageCount = pageList.size, state = pagerState) {
            getFormView(formPage = pageList[it])
        }
    }

    AnimatedVisibility(visible = !showPages, enter = fadeIn(), exit = fadeOut()) {
        baseState?.let { state ->
            GetStateComponent(state = state, action = {
                if (it is ViewModelBaseState.DataSavedState) {
                    navController.navigate(HOME_ROUTE)
                }
            })
        }
    }


    AnimatedVisibility(visible = requireEmailValidation == true, enter = fadeIn(), exit = fadeOut()) {
        StateComponent(message = "Você precisa validar seu email para criar uma receita", action = {
            newRecipeViewModel.pushValidationEmail()
        })
    }


    LaunchedEffect(Unit) {
        newRecipeViewModel.buildFirstPage()
    }

}
