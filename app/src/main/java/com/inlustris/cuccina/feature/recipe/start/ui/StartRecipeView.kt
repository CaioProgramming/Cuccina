@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class
)

package com.inlustris.cuccina.feature.recipe.start.ui

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.R
import com.inlustris.cuccina.feature.profile.ui.PROFILE_ROUTE_IMPL
import com.inlustris.cuccina.feature.recipe.domain.model.Recipe
import com.inlustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.inlustris.cuccina.feature.recipe.start.presentation.StartRecipeViewModel
import com.inlustris.cuccina.theme.StateComponent
import com.inlustris.cuccina.theme.GetStateComponent
import com.inlustris.cuccina.theme.PageIndicators
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.inlustris.cuccina.feature.recipe.category.ui.CATEGORY_ROUTE_IMPL
import com.inlustris.cuccina.feature.recipe.ui.component.RecipeAction
import com.inlustris.cuccina.theme.getPageView
import com.inlustris.cuccina.theme.pagerFadeTransition
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

const val START_RECIPE_ROUTE = "start_recipe/{recipeId}"
const val START_RECIPE_ROUTE_IMPL = "start_recipe/"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartRecipeView(
    recipeId: String? = null,
    startRecipeViewModel: StartRecipeViewModel? = null,
    navController: NavHostController
) {

    val state = startRecipeViewModel?.viewModelState?.observeAsState()
    val recipe = remember {
        mutableStateOf<Recipe?>(null)
    }
    val isFavorite = startRecipeViewModel?.isFavorite?.observeAsState()
    val isUserRecipe = startRecipeViewModel?.isUserRecipe?.observeAsState()
    val pages = startRecipeViewModel?.pages?.observeAsState()
    var progress by remember { mutableStateOf(0f) }
    var showTitle: Boolean by remember { mutableStateOf(true) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )

    var dropDownState by remember {
        mutableStateOf(false)
    }
    val dropDownOptions = listOf("Excluir")
    val dialogState = remember {
        mutableStateOf(false)
    }


    recipe.value?.let {
        if (pages?.value == null || pages.value?.isEmpty() == true) {
            startRecipeViewModel?.getPages(it)
        }

        AnimatedVisibility(
            visible = state?.value != ViewModelBaseState.LoadingState,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .animateContentSize(tween(1000))
                    .fillMaxSize()
            ) {
                val (title, pager, indicator, nextButton, progressBar, backButton, favoriteButton, alertDialog) = createRefs()

                pages?.value?.let { pages ->


                    fun getPageProgress(
                        currentPage: Int,
                        lastPage: Int
                    ) = (currentPage) / lastPage.toFloat()

                    val pagerState = rememberPagerState()
                    val scope = rememberCoroutineScope()

                    fun isComplete() =
                        pagerState.currentPage >= pages.filter { pageFilter -> pageFilter.showOnIndicator }.lastIndex

                    fun isLastPage() = pagerState.currentPage == pages.lastIndex
                    fun isFirstPage() = pagerState.currentPage == 0
                    val iconColor =
                        if (isComplete()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                    val backColor =
                        if (isComplete()) MaterialTheme.colorScheme.primary else Color.Transparent
                    val icon =
                        if (isComplete()) Icons.Default.Check else Icons.Default.KeyboardArrowRight


                    val iconColorAnimation by animateColorAsState(
                        targetValue = iconColor,
                        animationSpec = tween(1000)
                    )
                    val backColorAnimation by animateColorAsState(
                        targetValue = backColor,
                        animationSpec = tween(500)
                    )


                    val topIconColorAnimation by animateColorAsState(
                        targetValue = if (pagerState.currentPage == 0) {
                            MaterialColor.Black
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        animationSpec = tween(1500)
                    )
                    val topBackColorAnimation by animateColorAsState(
                        targetValue = if (pagerState.currentPage == 0) {
                            MaterialColor.White.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.background
                        },
                        animationSpec = tween(250)
                    )


                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.distinctUntilChanged()
                            .collect { page ->
                                progress = getPageProgress(
                                    page,
                                    pages.filter { pageFilter -> pageFilter.showOnIndicator }.lastIndex
                                )
                                showTitle = !isFirstPage()
                            }
                    }

                    BackHandler {
                        if (pagerState.currentPage > 0) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        } else {
                            navController.popBackStack()
                        }
                    }

                    fun handlePageAction(recipeAction: RecipeAction) {
                        when(recipeAction) {
                            is RecipeAction.OpenCategoryPage -> {
                                navController.navigate("$CATEGORY_ROUTE_IMPL${recipeAction.category.ordinal}")
                            }
                            is RecipeAction.OpenChefPage -> {
                                navController.navigate("$PROFILE_ROUTE_IMPL${recipeAction.chefId}")
                            }
                            RecipeAction.StartRecipe -> {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    }

                    HorizontalPager(
                        pageCount = pages.size,
                        userScrollEnabled = false,
                        state = pagerState,
                        modifier = Modifier.constrainAs(pager) {
                            if (isFirstPage()) {
                                top.linkTo(parent.top)
                            } else {
                                top.linkTo(title.bottom)
                            }
                            if (pagerState.currentPage != 0 && !isComplete()) {
                                bottom.linkTo(indicator.top)
                            } else {
                                bottom.linkTo(parent.bottom)
                            }
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        }) { index ->
                        getPageView(
                            page = pages[index],
                            pageModifier = Modifier.pagerFadeTransition(pagerState),
                            openRecipe = { id ->
                                navController.navigate(START_RECIPE_ROUTE_IMPL + id)
                            },
                            openChefPage = { chefId ->
                                navController.navigate("$PROFILE_ROUTE_IMPL$chefId")
                            },
                            navigateToNewRecipe = {
                                navController.navigate(NEW_RECIPE_ROUTE)
                            },
                            pageAction =  ::handlePageAction )
                    }

                    AnimatedVisibility(visible = showTitle,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(backButton.end)
                                end.linkTo(favoriteButton.start)
                                width = Dimension.fillToConstraints
                            }) {
                        Text(
                            text = it.name,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(16.dp),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                shadow = Shadow(
                                    color = MaterialColor.Black,
                                    offset = Offset(1f, 1f),
                                    blurRadius = 1.3f
                                )
                            )
                        )
                    }



                    AnimatedVisibility(
                        visible = dialogState.value,
                        enter = scaleIn(),
                        exit = slideOutVertically()
                    ) {
                        AlertDialog(modifier = Modifier
                            .constrainAs(alertDialog) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .wrapContentSize()
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                    defaultRadius
                                )
                            ),
                            onDismissRequest = {
                                dialogState.value = false
                            }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .wrapContentHeight(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Deseja excluir essa receita?",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Essa ação não pode ser desfeita",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Button(onClick = {
                                    recipeId?.let { recipe ->
                                        startRecipeViewModel.deleteData(recipe)
                                        dialogState.value = false
                                    }
                                }) {
                                    Text(text = "Excluir")
                                }

                                Button(
                                    onClick = {
                                        dialogState.value = false
                                    }, colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.5f
                                        )
                                    )
                                ) {
                                    Text(
                                        text = "Cancelar",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }

                            }
                        }
                    }


                    Row(modifier = Modifier
                        .constrainAs(favoriteButton) {
                            top.linkTo(title.top)
                            bottom.linkTo(title.bottom)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                        }
                        .padding(8.dp)
                        .animateContentSize()) {

                        IconButton(
                            onClick = {
                                startRecipeViewModel.favoriteRecipe(it)
                            }, modifier = Modifier
                                .padding(4.dp)
                                .background(topBackColorAnimation, CircleShape)
                        ) {
                            val favoriteIcon =
                                if (isFavorite?.value == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                            val color =
                                if (isFavorite?.value == true) MaterialColor.RedA400 else topIconColorAnimation
                            val description =
                                if (isFavorite?.value == true) "Desfavoritar" else "Favoritar"
                            Icon(
                                favoriteIcon,
                                tint = color,
                                contentDescription = description,
                            )
                        }

                        AnimatedVisibility(
                            visible = isUserRecipe?.value == true,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            IconButton(
                                onClick = {
                                    dropDownState = !dropDownState
                                }, modifier = Modifier
                                    .padding(4.dp)
                                    .background(topBackColorAnimation, CircleShape)
                            ) {
                                Icon(
                                    ImageVector.vectorResource(id = R.drawable.round_more_horiz_24),
                                    tint = topIconColorAnimation,
                                    contentDescription = "Opções",
                                )
                            }
                        }

                        DropdownMenu(expanded = dropDownState,
                            onDismissRequest = {
                                dropDownState = false
                            }) {
                            dropDownOptions.forEach { option ->
                                DropdownMenuItem(text = {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(8.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, onClick = {
                                    dialogState.value = true
                                    dropDownState = false

                                })
                            }
                        }

                    }

                    IconButton(modifier = Modifier
                        .constrainAs(backButton) {
                            top.linkTo(title.top)
                            bottom.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            height = Dimension.wrapContent
                        }
                        .padding(8.dp)
                        .background(topBackColorAnimation, CircleShape)
                        .animateContentSize(), onClick = {
                        scope.launch {
                            if (pagerState.currentPage > 0) {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Voltar",
                            tint = topIconColorAnimation,
                        )
                    }


                    AnimatedVisibility(visible = !isFirstPage() && !isComplete(),
                        enter = slideInHorizontally(tween(1000)),
                        exit = fadeOut(tween(250)),
                        modifier = Modifier
                            .constrainAs(indicator) {
                                bottom.linkTo(nextButton.bottom)
                                top.linkTo(nextButton.top)
                                start.linkTo(parent.start)
                                end.linkTo(nextButton.start)
                                width = Dimension.fillToConstraints
                            }
                            .wrapContentHeight()) {
                        PageIndicators(
                            count = pages.filter { pageFilter -> pageFilter.showOnIndicator }.size,
                            currentPage = pagerState.currentPage,
                            clearPreviousPage = false,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .wrapContentHeight(),
                            onSelectIndicator = {
                                scope.launch {
                                    pagerState.animateScrollToPage(it)
                                }
                            },
                            onFinishPageLoad = {
                                scope.launch {
                                    if (pagerState.currentPage != pages.lastIndex) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            }
                        )
                    }


                    AnimatedVisibility(visible = !isFirstPage(),
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .constrainAs(nextButton) {
                                top.linkTo(progressBar.top, 4.dp)
                                bottom.linkTo(progressBar.bottom, 4.dp)
                                start.linkTo(progressBar.start, 4.dp)
                                end.linkTo(progressBar.end, 4.dp)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .padding(8.dp)
                            .background(backColorAnimation, CircleShape)) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (!isLastPage()) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    } else {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            }) {
                            AnimatedContent(targetState = icon, transitionSpec = {
                                EnterTransition.None with ExitTransition.None
                            }) { target ->
                                Icon(
                                    target,
                                    modifier = Modifier.animateEnterExit(
                                        enter = scaleIn(),
                                        exit = scaleOut()
                                    ),
                                    contentDescription = if (!isComplete()) "Voltar" else "Finalizar Receita",
                                    tint = iconColorAnimation
                                )
                            }
                        }
                    }



                    AnimatedVisibility(
                        visible = !isFirstPage(),
                        enter = fadeIn(tween(500)),
                        exit = fadeOut(tween(1000)),
                        modifier = Modifier
                            .constrainAs(progressBar) {
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                                if (!isComplete()) {
                                    end.linkTo(parent.end)
                                } else {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                            }
                            .animateEnterExit(scaleIn(), scaleOut())
                            .size(70.dp)
                            .padding(8.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = progressAnimation,
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp,
                            strokeCap = StrokeCap.Round,
                        )
                    }


                } ?: kotlin.run {
                    StateComponent(message = "Carregando...")
                }
            }
        }


    } ?: run {
        Log.i("StarRecipeView", "StartRecipeView: querying recipe -> $recipeId")
        recipeId?.let { startRecipeViewModel?.getSingleData(it) }
    }

    state?.value?.run {
        when (this) {
            ViewModelBaseState.LoadingState -> StateComponent(message = "Carregando receita...")
            ViewModelBaseState.DataDeletedState -> GetStateComponent(state = this, action = {
                navController.popBackStack()
            })

            is ViewModelBaseState.DataRetrievedState -> {
                val foundedRecipe = this.data as Recipe
                recipe.value = foundedRecipe
            }

            is ViewModelBaseState.DataUpdateState -> {
                val updatedRecipe = this.data as Recipe
                recipe.value = updatedRecipe
                startRecipeViewModel.checkFavorite(updatedRecipe)
            }

            is ViewModelBaseState.ErrorState -> {
                StateComponent(
                    message = this.dataException.code.message,
                    buttonText = "Tentar novamente",
                    action = {
                        recipeId?.let { startRecipeViewModel.getSingleData(it) }
                    })
            }

            else -> {}

        }
    }


}

