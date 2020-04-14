package com.inlustris.cuccina.model

import android.app.Activity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.mateware.snacky.Snacky

abstract class ModelBase(val activity: Activity) : ModelContract, ValueEventListener {
    var errormessage = "Ocorreu um erro desconhecido..."
    var successmessage = "Operação concluída!"
    var reference = FirebaseDatabase.getInstance()


    fun success() {
        Snacky.builder().setActivity(activity).success().setText(successmessage)
    }

    fun success(message: String) {
        Snacky.builder().setActivity(activity).success().setText(message)
    }

    fun error() {
        Snacky.builder().setActivity(activity).error().setText(errormessage)
    }

    fun error(message: String) {
        Snacky.builder().setActivity(activity).error().setText("$errormessage (${message})")
    }


    var removedListener: DatabaseReference.CompletionListener = DatabaseReference.CompletionListener { databaseError, databaseReference ->
        if (databaseError != null) {
            error(databaseError.message)
        } else {
            success("Receita atualizada")
        }
    }
    var updateListener: DatabaseReference.CompletionListener = DatabaseReference.CompletionListener { databaseError, databaseReference ->
        if (databaseError != null) {
            error(databaseError.message)
        } else {
            success("Receita atualizada")
        }
    }
    var insertListener: DatabaseReference.CompletionListener = DatabaseReference.CompletionListener { databaseError, databaseReference ->
        if (databaseError != null) {
            error(databaseError.message)
        } else {
            success("Receita salva com sucesso!")
        }
    }
}