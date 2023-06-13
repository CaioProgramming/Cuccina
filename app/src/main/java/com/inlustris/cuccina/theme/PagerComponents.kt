@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.inlustris.cuccina.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.google.android.material.math.MathUtils
import kotlin.math.absoluteValue


fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

fun Modifier.pagerFadeTransition(pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = pagerState.calculateCurrentOffsetForPage(pagerState.currentPage)
        translationX = pageOffset * size.width
        alpha = 1 - pageOffset.absoluteValue
    }

fun Modifier.pagerScaleTransition(pagerState: PagerState) =
    graphicsLayer {
    // get a scale value between 1 and 1.75f, 1.75 will be when its resting,
    // 1f is the smallest it'll be when not the focused page
    val page = pagerState.currentPage
    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
    val scale = MathUtils.lerp(1f, 1.75f, pageOffset)
    // apply the scale equally to both X and Y, to not distort the image
    scaleX = scale
    scaleY = scale
}

fun Modifier.pagerCircularRevealTransition(state: PagerState) = graphicsLayer {
    // MAKE THE PAGE NOT MOVE
    val page = state.currentPage
    val pageOffset = state.offsetForPage(page)
    val offsetY = 0f
    translationX = size.width * pageOffset

    // ADD THE CIRCULAR CLIPPING
    val endOffset = state.endOffsetForPage(page)

    shadowElevation = 20f
    shape = CirclePath(
        progress = 1f - endOffset.absoluteValue,
        origin = Offset(
            size.width,
            offsetY,
        )
    )
    clip = true

    // PARALLAX SCALING
    val absoluteOffset = state.offsetForPage(page).absoluteValue
    val scale = 1f + (absoluteOffset.absoluteValue * .4f)

    scaleX = scale
    scaleY = scale

    // FADE AWAY
    val startOffset = state.startOffsetForPage(page)
    alpha = (2f - startOffset) / 2f
}

// ACTUAL OFFSET
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}

class CirclePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {

        val center = Offset(
            x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
            y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
        )
        val radius = (kotlin.math.sqrt(
            size.height * size.height + size.width * size.width
        ) * .5f) * progress

        return Outline.Generic(Path().apply {
            addOval(
                Rect(
                    center = center,
                    radius = radius.toFloat(),
                )
            )
        })
    }
}