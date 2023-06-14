@file:OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)

package com.inlustris.cuccina.feature.profile.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.inlustris.cuccina.feature.profile.presentation.ProfileViewModel
import com.inlustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.inlustris.cuccina.theme.StateComponent
import com.inlustris.cuccina.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.getDeviceMultiplier
import com.inlustris.cuccina.feature.profile.ui.component.SettingsSheet
import com.inlustris.cuccina.theme.getPageView
import com.inlustris.cuccina.theme.pagerFadeTransition
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

const val PROFILE_ROUTE = "profile/{userId}"
const val PROFILE_ROUTE_IMPL = "profile/"


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileView(
    userId: String? = null,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    val baseState = profileViewModel.viewModelState.observeAsState()
    val user = profileViewModel.user.observeAsState()
    val isUserPage = profileViewModel.isUserPage.observeAsState().value
    val pages = profileViewModel.pages.observeAsState()
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(Unit) {
        if (user.value == null)
            profileViewModel.getUserData(userId?.replace("{userId}", ""))
    }

    LaunchedEffect(user) {
        snapshotFlow { user.value }.distinctUntilChanged().collect {
            it?.let { user ->
                profileViewModel.getUserRecipes(user.uid)
                profileViewModel.getUserFavoriteRecipes(user.uid)
                systemUiController.isStatusBarVisible = false
            }
        }
    }

    fun openRecipe(recipeId: String) {
        navController.navigate("$START_RECIPE_ROUTE_IMPL${recipeId}")
    }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = baseState.value == ViewModelBaseState.LoadingState,
        enter = scaleIn(),
        exit = fadeOut(tween(1500))
    ) {
        CuccinaLoader(showText = false)
    }

    AnimatedVisibility(
        visible = baseState.value is ViewModelBaseState.ErrorState,
        enter = fadeIn()
    ) {
        val error = (baseState.value as ViewModelBaseState.ErrorState).dataException.code.message
        StateComponent(message = "Ocorreu um erro ao carregar suas informações\n($error)")
    }


    AnimatedVisibility(visible = pages.value != null, enter = fadeIn(), exit = fadeOut()) {
        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
        val scope = rememberCoroutineScope()

        fun showSheet() {
            scope.launch {
                sheetState.show()
            }
        }

        ModalBottomSheetLayout(
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetContent = {
                SettingsSheet()
            },
            sheetState = sheetState,
            sheetShape = MaterialTheme.shapes.large
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(tween(500))
            ) {

                val pagerState = rememberPagerState()
                val (pager, nextButton, nextTitle, settingsButton) = createRefs()
                val scope = rememberCoroutineScope()

                val isComplete = pagerState.currentPage == pages.value!!.lastIndex

                val iconColor =
                    if (pagerState.currentPage == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
                val backColor =
                    if (pagerState.currentPage == 0) MaterialTheme.colorScheme.primary else Color.Transparent
                val icon =
                    if (isComplete) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

                val iconColorAnimation by animateColorAsState(
                    targetValue = iconColor,
                    animationSpec = tween(500)
                )
                val backColorAnimation by animateColorAsState(
                    targetValue = backColor,
                    animationSpec = tween(250, easing = EaseInOut)
                )

                val nextPageTitle = if (!isComplete) {
                    pages.value!![pagerState.currentPage + 1].title
                } else {
                    ""
                }

                VerticalPager(
                    pageCount = pages.value!!.size,
                    state = pagerState,
                    modifier = Modifier.constrainAs(pager) {
                        if (isComplete) {
                            bottom.linkTo(parent.bottom)
                        } else {
                            bottom.linkTo(nextTitle.top)
                        }
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }) {
                    getPageView(
                        page = pages.value!![it],
                        pageModifier = Modifier.pagerFadeTransition(pagerState),
                        openRecipe = { id ->
                            openRecipe(id)
                        },
                        openChefPage = {},
                        navigateToNewRecipe = {})
                }


                AnimatedVisibility(visible = !isComplete,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .constrainAs(nextTitle) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()) {
                    Text(
                        text = nextPageTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                IconButton(modifier = Modifier
                    .constrainAs(nextButton) {
                        bottom.linkTo(parent.bottom, margin = 36.dp)
                        start.linkTo(pager.start)
                        end.linkTo(pager.end)
                    }
                    .padding(16.dp)
                    .size(90.dp, 50.dp)
                    .background(backColorAnimation, CircleShape),
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage != pages.value!!.lastIndex) {
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
                            modifier = Modifier
                                .animateEnterExit(
                                    enter = scaleIn(),
                                    exit = scaleOut()
                                )
                                .size(32.dp * getDeviceMultiplier()),
                            contentDescription = if (isComplete) "Voltar" else "Avançar",
                            tint = iconColorAnimation
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isUserPage ?: false && pagerState.currentPage == 0,
                    modifier = Modifier.constrainAs(settingsButton) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                    enter = scaleIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = {
                        showSheet()
                    },) {
                        Icon(
                            Icons.Filled.Settings,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Configurações"
                        )
                    }
                }


            }
        }


    }

}