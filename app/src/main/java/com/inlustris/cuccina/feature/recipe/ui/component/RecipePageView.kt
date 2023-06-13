@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package com.inlustris.cuccina.feature.recipe.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.inlustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.inlustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.inlustris.cuccina.feature.recipe.step.presentation.ui.InstructionItem
import com.inlustris.cuccina.theme.ExpandableComponent
import com.ilustris.cuccina.ui.theme.Page
import com.inlustris.cuccina.theme.annotatedPage
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.ilustris.cuccina.ui.theme.getDeviceMultiplier
import com.inlustris.cuccina.feature.recipe.form.ui.CaloriesInfoSheet
import com.inlustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientInfoSheet
import com.silent.ilustriscore.core.utilities.DateFormats
import com.silent.ilustriscore.core.utilities.format
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import java.util.*


sealed class RecipeAction() {
    data class OpenChefPage(val chefId: String) : RecipeAction()
    data class OpenCategoryPage(val category: Category) : RecipeAction()
    object StartRecipe : RecipeAction()
}

private const val INGREDIENT_SHEET = "INGREDIENTS"
private const val CALORIES_SHEET = "CALORIES"

@Composable
fun RecipePageView(
    page: Page.RecipePage,
    pageAction: (RecipeAction) -> Unit
) {

    var currentSheet by remember {
      mutableStateOf(INGREDIENT_SHEET)
    }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var currentIngredient by remember {
        mutableStateOf(page.recipe.ingredients.first())
    }
    val calories = page.recipe.calories

    @Composable
    fun selectedSheet() {
        when(currentSheet) {
            INGREDIENT_SHEET -> IngredientInfoSheet(ingredient = currentIngredient)
            CALORIES_SHEET -> CaloriesInfoSheet(calories = calories)
        }
    }

    fun showSheet(sheet: String) {
        currentSheet = sheet
        scope.launch {
            sheetState.show()
        }
    }

    ModalBottomSheetLayout(
        sheetContent = { selectedSheet() },
        sheetShape = MaterialTheme.shapes.large,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetState = sheetState,
    ) {
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            val recipe = page.recipe
            val formattedDate = Calendar.getInstance().apply {
                timeInMillis = recipe.publishDate
            }.time.format(DateFormats.DD_OF_MM_FROM_YYYY)

            item {
                    GlideImage(
                        modifier = Modifier
                            .height(300.dp * getDeviceMultiplier())
                            .fillMaxWidth(),
                        imageModel = { recipe.photo },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        ),
                        failure = {
                            Column(
                                modifier = Modifier
                                    .wrapContentSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Foto não encontrada",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.3f
                                        ), textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                            }
                        },
                        previewPlaceholder = R.drawable.ic_cherries
                    )
                    Text(
                        text = recipe.name.capitalize(Locale.current),
                        style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Start),
                        modifier = Modifier.padding(16.dp)
                    )
            }

            item {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    item {
                        val recipeCategory = Category.values().find { it.name == recipe.category }
                        recipeCategory?.let {
                            CategoryBadge(
                                category = it,
                                selectedCategory = recipeCategory,
                                categorySelected = {
                                    pageAction(RecipeAction.OpenCategoryPage(recipeCategory))
                                })
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .wrapContentSize(align = Alignment.CenterStart)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                        defaultRadius
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            val contentColor = MaterialTheme.colorScheme.onBackground
                            Icon(
                                Icons.Default.Favorite,
                                tint = contentColor,
                                contentDescription = "Favoritos",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = ("${recipe.likes.size}").uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                                color = contentColor
                            )
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .wrapContentSize(align = Alignment.CenterStart)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                        defaultRadius
                                    )
                                )
                                .clickable {
                                    showSheet(CALORIES_SHEET)
                                }
                                .padding(8.dp)
                                .clip(RoundedCornerShape(defaultRadius))

                        ) {
                            val contentColor = MaterialTheme.colorScheme.onBackground
                            Image(
                                painterResource(id = R.drawable.baseline_local_fire_department_24),
                                colorFilter = ColorFilter.tint(contentColor),
                                contentDescription = "Calorias",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = ("${recipe.calories}kcal").uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                                color = contentColor
                            )
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .wrapContentSize(align = Alignment.CenterStart)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                        defaultRadius
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            val contentColor = MaterialTheme.colorScheme.onBackground
                            Image(
                                painterResource(id = R.drawable.baseline_access_time_24),
                                colorFilter = ColorFilter.tint(contentColor),
                                contentDescription = "Tempo de preparo",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = ("${recipe.time} min").uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                                color = contentColor
                            )
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .wrapContentSize(align = Alignment.CenterStart)
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                        defaultRadius
                                    )
                                )
                                .padding(8.dp)
                        ) {
                            val contentColor = MaterialTheme.colorScheme.onBackground
                            Image(
                                painterResource(id = R.drawable.round_restaurant_menu_24),
                                colorFilter = ColorFilter.tint(contentColor),
                                contentDescription = "Porções",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                            Text(
                                text = ("${recipe.portions} porções").uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                                color = contentColor
                            )
                        }
                    }
                }
            }

            page.user?.let {
                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                            .clickable {
                                pageAction(RecipeAction.OpenChefPage(it.uid))
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = annotatedPage(
                                "${it.name} • $formattedDate",
                                annotations = listOf(it.name),
                                MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { pageAction(RecipeAction.StartRecipe) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(defaultRadius),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialColor.Black
                        ),
                    ) {
                        Text(
                            text = "Cozinhar",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 8.dp * getDeviceMultiplier())
                                .fillMaxWidth()
                        )
                    }
                }

            }

            item {
                Text(
                    text = recipe.description,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.7f
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                )

            }

            item {
                ExpandableComponent(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface, RoundedCornerShape(
                            defaultRadius
                        )
                    ), onExpandClick = {

                }, headerView = {
                    Column() {
                        Text(
                            text = "Ingredientes",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Essa receita precisa de ${recipe.ingredients.size} ingredientes",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }, innerContent = {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        val ingredients = recipe.ingredients
                        items(ingredients.size) { index ->
                            IngredientItem(ingredient = ingredients[index], longPress = { selectedIngredient ->
                                currentIngredient = selectedIngredient
                                showSheet(INGREDIENT_SHEET)

                            })

                        }
                    }
                })
            }

            item {
                ExpandableComponent(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface, RoundedCornerShape(
                            defaultRadius
                        )
                    ), onExpandClick = {}, headerView = {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = "Como fazer",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )

                        Text(
                            text = "Essa receita possui ${recipe.steps.size} etapas.",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }

                }, innerContent = {
                    Column() {
                        recipe.steps.forEachIndexed { index, step ->
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(
                                        MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.1f
                                        )
                                    )
                            )
                            ExpandableComponent(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                                    .fillMaxWidth(),
                                expanded = index == 0,
                                onExpandClick = {},
                                headerView = {
                                    Column(modifier = Modifier.padding(vertical = 16.dp)) {
                                        Text(
                                            text = step.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                        Text(
                                            text = "Essa etapa possui ${step.instructions.size} instruções.",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }

                                },
                                innerContent = {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        val instructions = step.instructions
                                        instructions.forEachIndexed { index, s ->
                                            InstructionItem(
                                                instruction = s,
                                                count = index + 1,
                                                savedIngredients = recipe.ingredients.map { it.name.lowercase() },
                                                isLastItem = index == instructions.size - 1,
                                                editable = false,
                                                onSelectInstruction = {})
                                        }
                                    }
                                })
                        }
                    }
                })
            }


        }
    }

}


