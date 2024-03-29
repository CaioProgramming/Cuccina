package com.inlustris.cuccina.feature.home.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.Page
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HighLightPage(page: Page.HighlightPage, modifier: Modifier) {

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (background, info) = createRefs()

        GlideImage(
            imageModel = { page.backgroundImage },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(
                    MaterialColor.Black.copy(alpha = 0.3f),
                    BlendMode.SrcAtop
                ),
            ), failure = {
                Text(
                    text = "Foto nao encontrada",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            },
            previewPlaceholder = R.drawable.ic_cherries,
            modifier = Modifier
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )

        Column(modifier = Modifier
            .constrainAs(info) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialColor.White
                )
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialColor.White
                )
            )
        }




    }

}