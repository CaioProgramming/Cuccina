package com.inlustris.cuccina

import android.util.Log

class IngredientsHelper {
    companion object {
        fun abreviate(unit: String?): String {
            Log.i(javaClass.simpleName, "Abreviating $unit")
            if (unit == null) return "medida não informada."
            when {
                unit.contains("grama", true) -> return "g"
                unit.contains("unidade", true) -> return "uni"
                unit.contains("kilo", true) -> return "kg"
                unit.contains("litro", true) -> return "l"
                unit.contains("mililitro", true) -> return "ml"
                unit.contains("colher de sopa", true) -> return "cs"
                unit.contains("colher de chá", true) -> return "chá"
                unit.contains("colher de sobremesa", true) -> return "csb"
                unit.contains("xícara de chá", true) -> return "xíc"
                unit.contains("xícara de café", true) -> return "xcf"
                unit.contains("copo", true) -> return "cp"
                unit.contains("peça", true) -> return "pç"
                unit.contains("pacote", true) -> return "pc"
                unit.contains("xícara", true) -> return "x"
                unit.contains("centímetros", true) -> return "cm"
                unit.contains("deciclitro", true) -> return "dl"
                unit.contains("centlitro", true) -> return "cl"
                else -> return unit
            }
        }

        fun meaning(unit: String?): String {
            Log.i(javaClass.simpleName, "Abreviating $unit")
            if (unit == null) return "medida não informada."
            when {
                unit.contains("g", true) -> return "grama(s)"
                unit.contains("uni", true) -> return "unidade(s)"
                unit.contains("kg", true) -> return "kilograma(s)"
                unit.contains("litro", true) -> return "litro(s)"
                unit.contains("ml", true) -> return "mililitro(s)"
                unit.contains("cs", true) -> return "colher de sopa"
                unit.contains("chá", true) -> return "colher de chá"
                unit.contains("csb", true) -> return "colher de sobremesa"
                unit.contains("xíc", true) -> return "xícara de chá"
                unit.contains("xcf", true) -> return "xícara de café"
                unit.contains("cp", true) -> return "copo"
                unit.contains("pç", true) -> return "peça"
                unit.contains("pc", true) -> return "pacote"
                unit.contains("x", true) -> return "xícara"
                unit.contains("cm", true) -> return "centímetros"
                unit.contains("dl", true) -> return "deciclitro"
                unit.contains("cl", true) -> return "centlitro"
                else -> return unit
            }
        }
    }


}