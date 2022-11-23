package com.silent.core.mock

import com.silent.core.data.Category
import com.silent.core.data.Recipe

object MockData {

    val recipesExamples = listOf<Recipe>(
        Recipe(
            name = "ESFERA DE CHOCOLATE RECHEADA COM MOUSSE E COCADA CREMOSA",
            description = "Esfera de chocolate gourmet e deliciosa, rende até 6 porções e queridinho por muitos confeiteiros.",
            image = "https://img.itdg.com.br/tdg/images/recipes/000/314/645/359166/359166_original.jpg?mode=crop&width=710&height=400"
        ),
        Recipe(
            name = "PAVÊ NATALINO DE MARACUJÁ COM PANETONE",
            image = "https://img.itdg.com.br/tdg/images/recipes/000/103/956/82400/82400_original.jpg?mode=crop&width=710&height=400"
        ),
        Recipe(
            name = "COSTELINHA COM MOLHO BARBECUE (OUTBACK)",
            image = "https://img.itdg.com.br/tdg/images/recipes/000/016/251/38806/38806_original.jpg?mode=crop&width=710&height=400"
        ),
        Recipe(
            name = "FRANGO FRITO AMERICANO",
            image = "https://img.itdg.com.br/tdg/images/recipes/000/089/261/47050/47050_original.jpg?mode=crop&width=710&height=400"
        ),
        Recipe(
            name = "FILÉ DE MERLUZA COM BATATA AO FORNO",
            image = "https://img.itdg.com.br/tdg/images/recipes/000/088/371/318327/318327_original.jpg?mode=crop&width=710&height=400"
        )
    )

    val categoriesExamples = listOf(
        Category(name = "Confeitaria", icon = "\uD83C\uDF82", color = "#ffeeff"),
        Category(name = "Carnes", icon = "\uD83E\uDD69", color = "#ff7961"),
        Category(name = "Massas", icon = "\uD83C\uDF5D", color = "#ff844c")
    )

}