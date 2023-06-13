package com.inlustris.cuccina.feature.profile.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.inlustris.cuccina.theme.CounterTextComponent
import com.inlustris.cuccina.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfilePageView(page: Page.ProfilePage) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                page.backColor ?: MaterialTheme.colorScheme.background,
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(16.dp)
    ) {
        val user = page.userModel
        GlideImage(
            imageModel = { user.photoUrl },
            imageOptions = ImageOptions(
                requestSize = IntSize(300, 300),
                alignment = Alignment.Center,
                contentDescription = user.name,
                contentScale = ContentScale.Fit
            ),
            loading = {
                CuccinaLoader()
            },
            modifier = Modifier
                .size(300.dp)
                .clip(CircleShape)
                .padding(16.dp)
                .border(
                    width = 5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ),
                    shape = CircleShape
                )
                .clip(CircleShape)
        )
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground
        Text(
            user.name,
            style = MaterialTheme.typography.headlineLarge.copy(
                color = textColor,
                fontWeight = FontWeight.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )


        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Center) {

            AnimatedVisibility(visible = page.postCount > 0, enter = fadeIn(), exit = fadeOut()) {
                CounterTextComponent(count = page.postCount, label = "Posts", textColor = textColor)

            }
            AnimatedVisibility(visible = page.favoriteCount > 0, enter = fadeIn(), exit = fadeOut()) {
                CounterTextComponent(
                    count = page.favoriteCount,
                    label = "Favoritos",
                    textColor = textColor
                )
            }

        }


    }
}




