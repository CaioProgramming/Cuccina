package com.inlustris.cuccina.feature.recipe.category.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.inlustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.inlustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.feature.recipe.ui.component.getStateComponent
import com.inlustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.inlustris.cuccina.feature.recipe.category.presentation.CategoryViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideRequestType

const val CATEGORY_ROUTE = "category/{categoryId}"
const val CATEGORY_ROUTE_IMPL = "category/"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryView(
    categoryId: Int = 0,
    categoryViewModel: CategoryViewModel,
    navController: NavHostController
) {

    val currentCategory = Category.values()[categoryId]

    val pageState = categoryViewModel.viewModelState.observeAsState()

    LaunchedEffect(Unit) {
        categoryViewModel.getCategoryRecipes(currentCategory.name)
    }

    fun navigateToRecipe(recipeId: String) {
        navController.navigate("$START_RECIPE_ROUTE_IMPL${recipeId}")
    }

    AnimatedVisibility(
        visible = pageState.value == ViewModelBaseState.LoadingState,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CuccinaLoader()

    }

    AnimatedVisibility(visible = pageState.value is ViewModelBaseState.ErrorState) {
        getStateComponent(state = pageState.value!!, action = {
            categoryViewModel.getCategoryRecipes(currentCategory.name)
        })
    }

    AnimatedVisibility(visible = pageState.value is ViewModelBaseState.DataListRetrievedState) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val coverSize = 350
            item {
                GlideImage(
                    imageModel = { currentCategory.cover },
                    glideRequestType = GlideRequestType.BITMAP,
                    imageOptions = ImageOptions(requestSize = IntSize(coverSize, coverSize)),
                    loading = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center) {
                            CuccinaLoader(false)

                        }
                    },
                    failure = {
                        Image(
                            painterResource(id = R.drawable.cherry),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            contentDescription = "Cuccina",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(coverSize.dp)
                )
            }
            stickyHeader {
                Text(
                    text = currentCategory.title,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(16.dp)
                )


            }
            item {
                Text(
                    text = currentCategory.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                )
            }
            item {
                Text(
                    text = "Receitas",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            val recipeList = (pageState.value as ViewModelBaseState.DataListRetrievedState).dataList as List<Recipe>

            items(recipeList) {
                RecipeCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp),
                    recipe = it,
                    onClickRecipe = { recipe ->
                        navigateToRecipe(recipe.id)
                    })
            }

        }

    }


}