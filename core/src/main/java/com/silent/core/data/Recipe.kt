package com.silent.core.data

import com.silent.ilustriscore.core.bean.BaseBean
import java.util.*

data class Recipe(
    override var id: String = "",
    var name: String = "",
    var description: String = "",
    var image: String = "",
    var category: String = "",
    var time: String = "",
    var steps: List<Step> = emptyList(),
    var ingredients: List<Ingredient> = emptyList(),
    val publishDate: Date = Date(),
) : BaseBean(id)

data class Step(var title: String, var description: String, var timer: Timer? = null)

data class Timer(val time: Long)

data class Ingredient(
    override var id: String,
    var name: String,
    var emoji: String,
    var quantity: Int,
    var measure: String
) : BaseBean(id)