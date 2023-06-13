@file:OptIn(ExperimentalAnimationApi::class)

package com.inlustris.cuccina.feature.home.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ilustris.cuccina.R
import com.inlustris.cuccina.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.ilustris.cuccina.ui.theme.getDeviceMultiplier
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun BannerCard(backgroundImage: String, onClickBanner: () -> Unit) {

    AnimatedContent(
        targetState = backgroundImage,
        transitionSpec = { EnterTransition.None with ExitTransition.None }) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(defaultRadius))
                .clickable {
                    onClickBanner()
                }
        ) {

            val (background, text) = createRefs()
            GlideImage(
                imageModel = { backgroundImage },
                loading = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center) {
                        CuccinaLoader(true)

                    }
                },
                imageOptions = ImageOptions(
                    colorFilter = ColorFilter.tint(
                        MaterialColor.Black.copy(alpha = 0.1f),
                        blendMode = BlendMode.SrcAtop
                    ),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ), failure = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center) {
                        CuccinaLoader(false)

                    }
                },
                previewPlaceholder = R.drawable.ic_cherries,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp * getDeviceMultiplier())
                    .blur(3.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .animateEnterExit(fadeIn(), fadeOut())
                    .clip(RoundedCornerShape(defaultRadius))
                    .constrainAs(background) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Column(modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }
                .fillMaxWidth()
                .padding(16.dp), verticalArrangement = Arrangement.Top) {

                val titleTextSize = MaterialTheme.typography.headlineMedium.fontSize * getDeviceMultiplier()
                val labelTextSize = MaterialTheme.typography.labelLarge.fontSize * getDeviceMultiplier()
                val textShadow =  Shadow(
                    color = MaterialColor.Black.copy(alpha = 0.5f),
                    offset = Offset(1f, -2f),
                    blurRadius = 10f
                )
                Text(
                    text = "De dar água na boca!",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = labelTextSize,
                        shadow = textShadow.copy(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    )
                )
                Text(
                    text = "Confira as favoritinhas da comunidade!",
                    fontSize = titleTextSize,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        shadow = textShadow
                    )
                )
            }


        }
    }

}

@Preview
@Composable
fun BannerPreview() {
    CuccinaTheme {
        BannerCard("") {}
    }
}