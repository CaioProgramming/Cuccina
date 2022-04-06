package com.silent.core.data

import com.silent.ilustriscore.core.bean.BaseBean

data class Recipe(
    override var id: String,
    var name: String,
    var image: String,
    var category: String,
    var time: String,
    var steps: List<Step>,
    var ingredients: List<Ingredient>
) : BaseBean(id)

data class Step(var title: String, var description: String)

data class Ingredient(
    override var id: String,
    var name: String,
    var emoji: String,
    var quantity: Int,
    var measure: String
) : BaseBean(id)