@file:OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)

package com.inlustris.cuccina.theme

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.*
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.ilustris.cuccina.ui.theme.getDeviceMultiplier
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlin.math.roundToInt


@Composable
fun appColors() =
    listOf(
        MaterialTheme.colorScheme.primary,
        MaterialColor.DeepOrange600,
        MaterialColor.PinkA700,
    )

@Composable
fun CuccinaLoader(showText: Boolean = true, customIcon: Int = R.drawable.cherry) {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        )
    )
    val brush = Brush.linearGradient(
        appColors(),
        start = Offset(offsetAnimation.value, offsetAnimation.value),
        end = Offset(x = offsetAnimation.value * 10, y = offsetAnimation.value * 5)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = ImageVector.vectorResource(id = customIcon),
            contentDescription = "Cuccina",
            modifier = Modifier
                .size(100.dp)
        )

        AnimatedVisibility(visible = showText, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = "Cuccina",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }


    }
}

@Composable
fun BuildtStateComponent(state: ViewModelBaseState, action: (ViewModelBaseState) -> Unit) {


    val message = when (state) {
        ViewModelBaseState.RequireAuth -> "Você precisa estar logado para acessar essa funcionalidade"
        ViewModelBaseState.DataDeletedState -> "Receita deletada com sucesso"
        ViewModelBaseState.LoadingState -> "Carregando..."
        ViewModelBaseState.LoadCompleteState -> "Carregamento completo"
        is ViewModelBaseState.DataRetrievedState -> "Receita carregada com sucesso"
        is ViewModelBaseState.DataListRetrievedState -> "Receitas carregadas com sucesso"
        is ViewModelBaseState.DataSavedState -> "Dados salvos com sucesso"
        is ViewModelBaseState.DataUpdateState -> "Dados atualizados com sucesso"
        is ViewModelBaseState.FileUploadedState -> "Arquivos enviados com sucesso"
        is ViewModelBaseState.ErrorState -> "Ocorreu um erro inesperado(${state.dataException.code.message}"
    }

    val buttonText = when (state) {
        ViewModelBaseState.RequireAuth -> "Fazer login"
        ViewModelBaseState.DataDeletedState -> "Ok"
        is ViewModelBaseState.ErrorState -> "Tentar novamente"
        else -> null
    }




    StateComponent(
        message = message,
        action = { action(state) },
        buttonText = buttonText
    )
}

@Preview(showBackground = true)
@Composable
fun CuccinaLoaderPreview() {
    CuccinaLoader()
}


fun annotatedPage(text: String, annotations: List<String>, color: Color) = buildAnnotatedString {
    append(text)
    Log.i("PageAnnotation", "annotatedPage: validating annotations -> $annotations on ($text)")
    annotations.forEach { annotation ->
        if (text.contains(annotation, true)) {
            val startIndex = text.indexOf(annotation)
            val endIndex = text.indexOf(annotation) + annotation.length
            if (startIndex != -1 && endIndex != text.length) {
                Log.i(javaClass.simpleName, "annotation: adding style to $annotation")
                addStyle(
                    SpanStyle(
                        color = color,
                        fontWeight = FontWeight.Bold
                    ),
                    start = text.indexOf(annotation),
                    end = (text.indexOf(annotation) + annotation.length)
                )
            }
        }
    }
}

@Composable
fun SimplePageView(page: Page.SimplePage, modifier: Modifier) {


    Column(
        modifier = modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground
        Text(
            text = page.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme
                .typography
                .headlineLarge
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
        Text(
            text = annotatedPage(
                page.description,
                page.annotatedTexts,
                MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
    }
}

@Composable
fun AnimatedTextPage(page: Page.AnimatedTextPage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
    ) {
        var texts = page.texts.toString().replace("[", "").replace("]", "").replace(",", "")
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground

        repeat(3 * page.texts.size) {
            texts += texts
        }

        Text(
            texts,
            letterSpacing = 3.sp,
            lineHeight = 50.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .graphicsLayer {
                    val scale = 2.5f
                    scaleX = scale
                    scaleY = scale
                }
                .fillMaxSize()
        )



        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .headlineLarge
                    .copy(
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        )
                    )
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .bodyMedium
                    .copy(
                        textAlign = TextAlign.Center,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        ),
                        color = textColor
                    )
            )

        }


    }
}

@Composable
fun SuccessPageView(
    page: Page.SuccessPage,
    modifier: Modifier = Modifier,
    pageAction: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground

        val celebrateComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.happytoast)
        )

        val celebrateProgress by animateLottieCompositionAsState(
            celebrateComposition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            celebrateComposition,
            celebrateProgress,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )


        Text(
            text = page.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme
                .typography
                .headlineLarge
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
        Text(
            text = page.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(textAlign = TextAlign.Center, color = textColor)
        )

        Button(
            onClick = pageAction,
            shape = RoundedCornerShape(defaultRadius),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp * getDeviceMultiplier())
        ) {
            Text(
                text = page.actionText
            )
        }
    }
}

@Composable
fun ExpandableComponent(
    modifier: Modifier,
    expanded: Boolean = false,
    onExpandClick: (Boolean) -> Unit,
    headerView: @Composable () -> Unit,
    innerContent: @Composable () -> Unit
) {

    var expanded by remember {
        mutableStateOf(expanded)
    }

    AnimatedContent(targetState = expanded, modifier = modifier, transitionSpec = {
        fadeIn() with fadeOut()
    }) {
        ConstraintLayout(modifier = Modifier.animateContentSize()) {
            val (icon, header, content) = createRefs()
            IconButton(onClick = {
                expanded = !expanded
                onExpandClick(expanded)
            }, modifier = Modifier.constrainAs(icon) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }) {
                val iconRes = if (expanded) {
                    Icons.Rounded.KeyboardArrowUp
                } else {
                    Icons.Rounded.KeyboardArrowDown
                }
                val description = if (expanded) {
                    "Fechar"
                } else {
                    "Abrir"
                }
                Icon(
                    iconRes,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = description
                )
            }
            Box(modifier = Modifier.constrainAs(header) {
                start.linkTo(parent.start)
                end.linkTo(icon.start)
                top.linkTo(parent.top)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }) {
                headerView()
            }

            Box(modifier = Modifier.constrainAs(content) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(header.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }) {
                AnimatedVisibility(visible = expanded, enter = fadeIn(), exit = fadeOut()) {
                    innerContent()
                }
            }

        }

    }



}

@Composable
fun CounterTextComponent(count: Int, label: String, textColor: Color) {
    var countValue by remember { mutableStateOf(0) }
    val postCounter by animateFloatAsState(
        targetValue = countValue.toFloat(),
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        )
    )
    LaunchedEffect(Unit) {
        countValue = count
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
        Text(
            postCounter.roundToInt().toString(),
            color = textColor,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            label,
            color = textColor.copy(alpha = 0.6f),
            style = MaterialTheme.typography.labelSmall
        )
    }
}


@Preview
@Composable
fun ExpandCardPreview() {
    CuccinaTheme() {
        ExpandableComponent(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                    defaultRadius
                )
            ), onExpandClick = {}, headerView = { Text(text = "Expand card") }) {
            Text(text = "Inner content")
        }
    }
}