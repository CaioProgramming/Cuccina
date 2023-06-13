@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class, ExperimentalAnimationApi::class,
    ExperimentalAnimationApi::class
)

package com.ilustris.cuccina.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.ui.theme.Page
import com.inlustris.cuccina.theme.PageIndicators
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.inlustris.cuccina.theme.getPageView
import com.inlustris.cuccina.theme.pagerCircularRevealTransition
import com.inlustris.cuccina.theme.pagerFadeTransition
import com.inlustris.cuccina.theme.pagerScaleTransition
import kotlinx.coroutines.launch


@Composable
fun HighLightSheet(
    pages: List<Page>,
    autoSwipe: Boolean = false,
    closeButton: () -> Unit,
    openRecipe: (String) -> Unit,
    openNewRecipe: () -> Unit,
    openChefPage: (String) -> Unit,
) {
    ConstraintLayout {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        val (pager, indicators, button, closeButton) = createRefs()
        fun currentPage() = pages[pagerState.currentPage]

        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            modifier = Modifier
                .constrainAs(pager) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        ) { index ->
            getPageView(
                page = pages[index],
                pageModifier = Modifier.pagerScaleTransition(pagerState),
                openRecipe,
                openChefPage,
                openNewRecipe
            )
        }

        PageIndicators(
            count = pages.size,
            currentPage = pagerState.currentPage,
            enableAutoSwipe = autoSwipe,
            clearPreviousPage = false,
            modifier = Modifier
                .constrainAs(indicators) {
                    top.linkTo(closeButton.top)
                    bottom.linkTo(closeButton.bottom)
                    start.linkTo(closeButton.end)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(16.dp), onSelectIndicator = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }, onFinishPageLoad = {
                scope.launch {
                    if (pagerState.currentPage < pages.size - 1)
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            })


        IconButton(modifier = Modifier.constrainAs(closeButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }, onClick = {
            closeButton()
        }) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Fechar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        AnimatedVisibility(
            visible = currentPage() is Page.HighlightPage,
            enter = scaleIn(),
            exit = scaleOut(),
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
        ) {
            Button(
                shape = RoundedCornerShape(defaultRadius),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(8.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                onClick = {
                    val page = currentPage()
                    if (page is Page.HighlightPage) {
                        openRecipe(page.recipeId)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(text = "Ver Receita", fontWeight = FontWeight.Bold)
                    Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = null)
                }
            }
        }


    }

}


