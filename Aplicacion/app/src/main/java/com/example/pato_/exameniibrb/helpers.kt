package com.example.pato_.exameniibrb

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.arch.core.util.Function
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import com.beust.klaxon.Klaxon
import com.example.andres.examenapp2.BDD.Companion.aplicaciones
import com.example.andres.examenapp2.BDD.Companion.sistemasOperativos
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf



fun mensaje(actividad:Activity,tipo: String,contenido:String){

    com.tapadoo.alerter.Alerter.create(actividad)
            .setTitle(tipo)
            .setText(contenido)
            .show()
}


fun mensaje_dialogo(actividad:Activity,contenido:String,funcion: () -> Unit){
    val builder = AlertDialog.Builder(actividad)

    builder
            .setMessage(contenido)
            .setPositiveButton(
                    "Si",
                    DialogInterface.OnClickListener { dialog, which ->
                        funcion()
                    }
            )
            .setNegativeButton(
                    "No",null
            )


    val dialogo = builder.create()
    dialogo.show()
    dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)
    dialogo.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE)
}

fun cargarDatosSO(url:String,funcion_intent: () -> Unit){
    url.httpGet().responseString{request, response, result ->
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                Log.i("http", ex.toString())
            }
            is Result.Success -> {
                val data = result.get()
                sistemasOperativos.clear()
                val wordDict = Klaxon().parseArray<SistemaOperativo>(data)
                Log.i("http", "Datos: ${wordDict.toString()}")
                if (wordDict != null) {
                    for ( item in wordDict.iterator()){
                        sistemasOperativos.add(item)
                    }
                }

                funcion_intent()

            }
        }
    }



}

fun cargarDatosApp(url:String,funcion_intent: () -> Unit){
    url.httpGet().responseString{request, response, result ->
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                Log.i("http", ex.toString())
            }
            is Result.Success -> {
                val data = result.get()
                aplicaciones.clear()
                val wordDict = Klaxon().parseArray<Aplicacion>(data)
                Log.i("http", "Datos: ${wordDict.toString()}")
                if (wordDict != null) {
                    for ( item in wordDict.iterator()){
                        aplicaciones.add(item)
                    }
                }

                funcion_intent()

            }
        }
    }
}
