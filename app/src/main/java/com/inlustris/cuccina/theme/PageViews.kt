@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalPagerApi::class
)

package com.inlustris.cuccina.theme

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.inlustris.cuccina.feature.home.ui.component.HighLightPage
import com.inlustris.cuccina.feature.profile.ui.component.ChefsPageView
import com.inlustris.cuccina.feature.profile.ui.component.ProfilePageView
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.inlustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientsPageView
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepPageView
import com.inlustris.cuccina.feature.recipe.step.presentation.ui.StepsPageView
import com.inlustris.cuccina.feature.recipe.ui.component.RecipePageView
import com.inlustris.cuccina.feature.recipe.ui.component.RecipesPageView
import com.ilustris.cuccina.ui.theme.AnimatedTextPage
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.SimplePageView
import com.ilustris.cuccina.ui.theme.SuccessPageView
import com.inlustris.cuccina.feature.recipe.ui.component.RecipeAction

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    CuccinaTheme {
        val pages = listOf(
            Page.SimplePage("Vamos cozinhar?", "Prepare sua cozinha e vamos começar!"),
            Page.AnimatedTextPage(
                "Vamos cozinhar?",
                description = "Prepare sua cozinha e vamos começar!",
                IngredientMapper.emojiList().take(3)
            )
        )

        getPageView(
            page = pages.last(),
            openRecipe = {},
            openChefPage = {},
            navigateToNewRecipe = {})
    }

}

@Composable
fun PageIndicators(
    count: Int,
    currentPage: Int,
    modifier: Modifier,
    enableAutoSwipe: Boolean = false,
    clearPreviousPage: Boolean = true,
    onFinishPageLoad: (Int) -> Unit,
    onSelectIndicator: (Int) -> Unit
) {


    fun pageFillRequired(index: Int): Boolean {
        return if (clearPreviousPage) currentPage == index else currentPage >= index
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(count) { index ->

            val delay = if (!pageFillRequired(index)) 500 else 10000
            var progressTarget by remember {
                mutableStateOf(0f)
            }

            val animateProgress by animateFloatAsState(
                targetValue = progressTarget,
                animationSpec = tween(
                    durationMillis = delay,
                    delayMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )

            val weight = 1f / count

            progressTarget = if (pageFillRequired(index)) {
                1f
            } else {
                0f
            }

            LinearProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                color = MaterialTheme.colorScheme.secondary,
                strokeCap = StrokeCap.Round,
                progress = animateProgress,
                modifier = Modifier
                    .padding(1.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .weight(weight)
                    .height(5.dp)
                    .clickable {
                        onSelectIndicator(index)
                    }
            )

            LaunchedEffect(animateProgress) {
                snapshotFlow { animateProgress }.collect { progress ->
                    if (progress == 1f && enableAutoSwipe) {
                        onFinishPageLoad(index)
                    }
                }
            }
        }
    }
}


@Composable
fun getPageView(
    page: Page,
    pageModifier: Modifier = Modifier,
    openRecipe: ((String) -> Unit)? = null,
    openChefPage: ((String) -> Unit)? = null,
    navigateToNewRecipe: (() -> Unit)? = null,
    pageAction: ((RecipeAction) -> Unit)? = null
) {

    return when (page) {
        is Page.SimplePage -> SimplePageView(page, pageModifier)
        is Page.StepsPage -> StepsPageView(page)
        is Page.IngredientsPage -> IngredientsPageView(page)
        is Page.StepPage -> StepPageView(page)
        is Page.RecipePage -> RecipePageView(
            page,
            pageAction = { pageAction?.invoke(it) }
        )

        is Page.HighlightPage -> HighLightPage(page = page, modifier = pageModifier)
        is Page.AnimatedTextPage -> AnimatedTextPage(page = page, pageModifier)
        is Page.ProfilePage -> ProfilePageView(page = page)
        is Page.RecipeListPage -> RecipesPageView(
            page = page,
            openRecipe = { openRecipe?.invoke(it) })

        is Page.SuccessPage -> SuccessPageView(
            page = page,
            modifier = pageModifier
        ) { navigateToNewRecipe?.invoke() }

        is Page.OtherChefsPage -> ChefsPageView(page = page, openChefPage = { openChefPage?.invoke(it) })
        else -> {}
    }
}



