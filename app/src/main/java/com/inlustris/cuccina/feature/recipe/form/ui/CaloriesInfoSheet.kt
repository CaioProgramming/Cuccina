package com.inlustris.cuccina.feature.recipe.form.ui

import ai.atick.material.MaterialColor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ilustris.cuccina.R

@Composable
fun CaloriesInfoSheet(calories: Int) {
    Column(modifier = Modifier.padding(16.dp)) {
        val caloriesComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.fire)
        )

        val celebrateProgress by animateLottieCompositionAsState(
            caloriesComposition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )

        val caloriesBrush = listOf(
            MaterialColor.RedA400,
            MaterialColor.OrangeA400,
            MaterialColor.YellowA700,
        )

        LottieAnimation(
            caloriesComposition,
            celebrateProgress,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Text(
            text = "${calories} kcal",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp).graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                caloriesBrush
                            ),
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                }
        )

        Text(
            text = "Valores aproximados por porção, consulte um nutricionista para mais informações.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}