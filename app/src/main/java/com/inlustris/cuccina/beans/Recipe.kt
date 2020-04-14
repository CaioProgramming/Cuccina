package com.inlustris.cuccina.beans

import java.io.Serializable

class Recipe : Serializable {
    var prato: String? = ""
    var tempo: String? = ""
    var id: String? = ""
    var tipo: String? = ""
    var imageurl: String? = ""
    var calorias: String? = ""


    constructor()
    constructor(nome: String?, tipo: String?, imageurl: String?, calorias: String?, tempo: String?) {
        prato = nome
        this.tipo = tipo
        this.imageurl = imageurl
        this.calorias = calorias
        this.tempo = tempo
    }

    fun isLastRecipe(): Boolean {
        return id.equals("Last Recipe")
    }

    companion object {
        fun lastRecipe(): Recipe {
            var recipe = Recipe()
            recipe.id = "Last Recipe"
            return recipe
        }
    }


}