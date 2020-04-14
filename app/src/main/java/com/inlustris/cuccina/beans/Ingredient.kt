package com.inlustris.cuccina.beans


class Ingredient {
    var count: String? = null


    var ingrediente: String? = null
    var medidas: String? = null
    var quantidade: String? = null

    constructor()
    constructor(count: String?, ingrediente: String?, medidas: String?, quantidade: String?) {
        this.count = count
        this.ingrediente = ingrediente
        this.medidas = medidas
        this.quantidade = quantidade
    }

}