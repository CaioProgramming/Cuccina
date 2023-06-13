@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.inlustris.cuccina.feature.recipe.ingredient.presentation.ui

import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientType
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Texture
import com.ilustris.cuccina.ui.theme.defaultRadius

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun IngredientInfoSheet(ingredient: Ingredient) {


    fun getIngredientEmoji(ingredientName: String) =
        IngredientMapper.getIngredientSymbol(ingredientName)


    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        val targetEmoji = getIngredientEmoji(ingredient.name)

        AnimatedContent(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            targetState = targetEmoji,
            transitionSpec = {
                EnterTransition.None with ExitTransition.None
            }) {
            Text(
                text = targetEmoji,
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier
                    .animateEnterExit(enter = scaleIn(), exit = scaleOut())
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(8.dp)
                    .animateContentSize()
            )
        }

        Text(
            ingredient.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W700
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        val quantityText = if (ingredient.type == IngredientType.TASTE) {
            ingredient.type.description
        } else {
            "${ingredient.quantity} ${ingredient.type.description}"
        }

        Text(
            text = quantityText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )


    }

}

@Preview(showBackground = true)
@Composable
fun IngredientInfoSheetPreview() {
    IngredientInfoSheet(
        Ingredient(
            "Alcatra",
            300,
            IngredientType.KILOGRAMS
        )
    )
}