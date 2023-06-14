package com.inlustris.cuccina.feature.profile.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inlustris.cuccina.feature.profile.domain.model.UserModel
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import com.inlustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.ui.theme.Page
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val recipeService: RecipeService,
    override val service: UserService
) : BaseViewModel<UserModel>(application) {


    val user = MutableLiveData<UserModel>()
    val pages = MutableLiveData<ArrayList<Page>>()
    val isUserPage = MutableLiveData(false)

    private fun updatePages(page: Page) {
        pages.value?.let {
            it.add(page)
            pages.postValue(it)
        } ?: kotlin.run {
            pages.postValue(ArrayList(listOf(page)))
        }
    }


    fun getUserRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val queryTask = recipeService.getRecipesByUser(userID)) {
                is ServiceResult.Error -> {
                    val title = if (isUserPage.value == true) {
                        "Minhas receitas"
                    } else {
                        "Receitas de ${user.value?.name}"
                    }

                    val message = if (isUserPage.value == true) {
                        "Você ainda não tem receitas publicadas. \nQue tal começar agora mesmo?"
                    } else {
                        "${user.value?.name} ainda não tem receitas publicadas."
                    }
                    updatePages(
                        Page.SimplePage(
                            title,
                            message,
                            listOf("receitas")
                        )
                    )
                    Log.e(
                        javaClass.simpleName,
                        "getUserRecipes: error ${queryTask.errorException.code}"
                    )
                }

                is ServiceResult.Success -> {
                    val message = if (isUserPage.value == true) {
                        "Desde que entrou no Cuccina você publicou ${queryTask.data.size} receitas.\nContinue assim!"
                    } else {
                        "${user.value?.name} publicou ${queryTask.data.size} receitas."
                    }
                    updatePages(
                        Page.RecipeListPage(
                            "Minhas receitas",
                            message,
                            queryTask.data as List<Recipe>
                        )
                    )
                    pages.value?.run {
                        val profileIndex = indexOfFirst { it is Page.ProfilePage }
                        val profilePage = this[profileIndex] as Page.ProfilePage
                        profilePage.postCount = queryTask.data.size
                        this[profileIndex] = profilePage
                        pages.postValue(this)
                    }

                }
            }
        }
    }

    fun getUserFavoriteRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val queryTask = recipeService.getRecipesByUserLike(userID)) {
                is ServiceResult.Error -> {
                    val message = if (isUserPage.value == true) {
                        "Você ainda não tem receitas favoritas. \nQue tal dar uma olhada nas receitas e favoritar as que mais gostar?"
                    } else {
                        "Este usuário ainda não tem receitas favoritas."
                    }
                    updatePages(
                        Page.SimplePage(
                            "Receitas favoritas",
                            message,
                            annotatedTexts = listOf("favoritas")
                        )
                    )
                    Log.e(
                        javaClass.simpleName,
                        "getUserFavoriteRecipes: error ${queryTask.errorException.code}",
                    )
                }

                is ServiceResult.Success -> {
                    val message = if (isUserPage.value == true) {
                        "Você tem ${queryTask.data.size} receitas favoritas. Esperamos que encontre mais receitas que goste!"
                    } else {
                        "${user.value?.name} tem ${queryTask.data.size} receitas favoritas."
                    }
                    updatePages(
                        Page.RecipeListPage(
                            "Receitas favoritas",
                            message,
                            queryTask.data as List<Recipe>
                        )
                    )
                    pages.value?.run {
                        val profileIndex = indexOfFirst { it is Page.ProfilePage }
                        val profilePage = this[profileIndex] as Page.ProfilePage
                        profilePage.favoriteCount = queryTask.data.size
                        this[profileIndex] = profilePage
                        pages.postValue(this)
                    }
                }
            }
            updateViewState(ViewModelBaseState.LoadCompleteState)
        }
    }

    fun getUserData(userId: String?) {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            service.currentUser()?.let {
                val uid = if (userId.isNullOrEmpty()) it.uid else userId
                when (val userTask = service.getSingleData(uid)) {
                    is ServiceResult.Success -> {
                        delay(1000)
                        user.postValue(userTask.data as UserModel)
                        isUserPage.postValue(userId == service.currentUser()?.uid || userId.isNullOrEmpty())
                        val profilePage = Page.ProfilePage(userModel = userTask.data as UserModel)
                        updatePages(profilePage)
                    }

                    is ServiceResult.Error -> {
                        updateViewState(ViewModelBaseState.ErrorState(userTask.errorException))
                    }
                }
            }
        }
    }



}