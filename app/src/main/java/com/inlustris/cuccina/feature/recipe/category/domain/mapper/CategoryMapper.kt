package com.inlustris.cuccina.feature.recipe.category.domain.mapper

import ai.atick.material.MaterialColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.inlustris.cuccina.feature.recipe.category.domain.model.Category

object CategoryMapper {

    fun findCategory(name: String) = Category.values().find { it.name == name } ?: Category.UNKNOWN


    @Composable
    fun getCategoryColor(category: Category): Color {
        return when (category) {
            Category.DRINKS -> MaterialColor.Blue200
            Category.CANDY -> MaterialColor.Pink200
            Category.SOUPS -> MaterialColor.Purple200
            Category.SEA -> MaterialColor.Teal200
            Category.PASTA -> MaterialColor.Red100
            Category.SAUCE -> MaterialColor.Red300
            Category.PROTEIN -> MaterialColor.Red300
            Category.SALADS -> MaterialColor.LightGreen200
            Category.UNKNOWN -> MaterialColor.BlueGray200
        }
    }

}