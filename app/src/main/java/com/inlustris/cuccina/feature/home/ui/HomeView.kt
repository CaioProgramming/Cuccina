@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class
)

package com.inlustris.cuccina.feature.home.ui

import ai.atick.material.MaterialColor
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ilustris.cuccina.R
import com.inlustris.cuccina.feature.home.presentation.HomeViewModel
import com.ilustris.cuccina.feature.home.ui.HighLightSheet
import com.inlustris.cuccina.feature.home.ui.component.BannerCard
import com.inlustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.inlustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.inlustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.inlustris.cuccina.feature.recipe.ui.RecipeGroupList
import com.inlustris.cuccina.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.inlustris.cuccina.theme.BuildtStateComponent
import com.inlustris.cuccina.feature.recipe.category.ui.CATEGORY_ROUTE_IMPL
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*

const val HOME_ROUTE = "home"
private const val HIGHLIGHT_SHEET = "HIGHGLIGHT_SHEET"
private const val SETTINGS_SHEET = "SETTINGS_SHEET"

@Composable
fun HomeView(homeViewModel: HomeViewModel?, navController: NavHostController) {
    val homeBaseState = homeViewModel?.viewModelState?.observeAsState()
    val homeList = homeViewModel?.homeList?.observeAsState()
    val highLights = homeViewModel?.highlightRecipes?.observeAsState()
    val categories = Category.values().toList().filter { it.title != "Outros" }.sortedBy { it.title }
    val systemUiController = rememberSystemUiController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    var query by remember {
        mutableStateOf("")
    }

    var currentSheet by remember {
        mutableStateOf(HIGHLIGHT_SHEET)
    }


    LaunchedEffect(Unit) {
        homeViewModel?.loadHome()
    }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.currentValue }.distinctUntilChanged().collect {
            systemUiController.isStatusBarVisible = it == ModalBottomSheetValue.Hidden || currentSheet == SETTINGS_SHEET
        }
    }

    fun navigateToRecipe(recipeId: String) {
        navController.navigate("$START_RECIPE_ROUTE_IMPL${recipeId}")
    }


    @Composable
    fun getCurrentSheet() {
        when (currentSheet) {
            HIGHLIGHT_SHEET -> {
                highLights?.value?.let {
                    HighLightSheet(pages = it, autoSwipe = sheetState.isVisible, closeButton = {
                        scope.launch {
                            sheetState.hide()
                        }
                    }, openRecipe = { id ->
                        navigateToRecipe(id)
                    }, openNewRecipe = {
                        navController.navigate(NEW_RECIPE_ROUTE)
                    }, openChefPage = { chefId -> })
                }
            }


        }
    }

    fun showSheet(requiredSheet: String) {
        scope.launch {
            currentSheet = requiredSheet
            sheetState.show()
        }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.animateContentSize(tween(500)).fillMaxSize(),
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = { getCurrentSheet() }) {

        BackHandler {
            if (sheetState.isVisible) {
                scope.launch {
                    sheetState.hide()
                }
            } else {
                navController.popBackStack()
            }
        }

        fun isLoading() = homeBaseState?.value == ViewModelBaseState.LoadingState && homeList?.value == null

        val context = LocalContext.current

        AnimatedVisibility(
            visible = isLoading(),
            enter = fadeIn(tween(500)),
            exit = scaleOut(tween(500))
        ) {
            CuccinaLoader()
        }

        AnimatedVisibility(
            visible = !isLoading(),
            enter = fadeIn(
                tween(1500)
            ),
            exit = fadeOut(tween(500))
        ) {


            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                stickyHeader {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painterResource(id = R.drawable.cherry),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                    contentDescription = "Cuccina",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(4.dp)
                                )
                                Text(
                                    text = "Cuccina",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black
                                    )
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                }

                item {
                    TextField(
                        value = query,
                        leadingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = "Pesquisar")
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = query.isNotEmpty(),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    Icons.Sharp.Close,
                                    contentDescription = "fechar",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                            query = ""
                                            homeViewModel?.searchRecipe(query)
                                        }
                                )
                            }

                        },
                        onValueChange = {
                            query = it
                        },
                        placeholder = {
                            Text(
                                text = "Busque por receitas ou ingredientes...",
                                maxLines = 1,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        shape = RoundedCornerShape(defaultRadius),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search,
                            autoCorrect = true
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            homeViewModel?.searchRecipe(query)
                        }),
                        maxLines = 1,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                item {

                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        LazyRow(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {

                            items(categories.size) {
                                CategoryBadge(
                                    category = categories[it],
                                    selectedCategory = null,
                                ) { category ->
                                    navController.navigate("${CATEGORY_ROUTE_IMPL}${category.ordinal}")

                                }
                            }
                        }
                    }
                }

                highLights?.value?.let { highLights ->
                    item {
                        val highLightPage =
                            highLights.find { it is Page.HighlightPage } as? Page.HighlightPage
                        highLightPage?.let { page ->
                            BannerCard(page.backgroundImage) {
                                showSheet(HIGHLIGHT_SHEET)
                            }
                        }

                    }
                }

                homeBaseState?.value?.let {
                    when (it) {
                        ViewModelBaseState.LoadCompleteState -> {
                            Log.i(javaClass.simpleName, "HomeView: Load complete")
                        }

                        else -> {
                            item {
                                BuildtStateComponent(state = it) { state ->
                                    if (state is ViewModelBaseState.ErrorState) {
                                        homeViewModel.loadHome()
                                    }
                                }
                            }
                        }
                    }
                }

                Log.i(javaClass.simpleName, "HomeView: ${homeList?.value} ")

                homeList?.value?.let {
                    items(it.size) { index ->
                        val group = it[index]
                        RecipeGroupList(
                            recipeGroup = group,
                            orientation = RecyclerView.HORIZONTAL
                        ) { recipe ->
                            navigateToRecipe(recipe.id)
                        }
                    }
                }

                fun openPlayStorePage() {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/dev?id=8106172357045720296")
                    )
                    context.startActivity(intent)
                }

                if (homeList?.value?.isNotEmpty() == true) {

                    val ilustrisBrush = listOf(
                        MaterialColor.TealA400,
                        MaterialColor.BlueA400,
                        MaterialColor.LightBlueA700,
                    )

                    item {
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        Text(
                            text = "Todas as receitas foram obtidas através de sites públicos e não possuem fins lucrativos.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
                        )
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        openPlayStorePage()
                                    }
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(
                                                brush = Brush.linearGradient(
                                                    ilustrisBrush
                                                ),
                                                blendMode = BlendMode.SrcAtop
                                            )
                                        }
                                    }
                            )
                        }

                        Text(
                            text = "Desenvolvido por ilustris em 2019 - $currentYear",
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
                                .clickable { openPlayStorePage() },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        )
                    }
                }

            }
        }


    }


}
