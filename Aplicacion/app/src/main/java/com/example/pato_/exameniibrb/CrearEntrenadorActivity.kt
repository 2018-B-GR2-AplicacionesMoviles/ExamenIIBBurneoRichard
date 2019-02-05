package com.example.pato_.exameniibrb

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.andres.examenapp2.BDD.Companion.ip
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_crear_so.*

class CrearEntrenadorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_so)
        val sistema = intent.getParcelableExtra<EntrenadorSe?>("sistema")
        //val sistema: SistemaOperativo? = null
        var esnuevo = true

        if (sistema != null) {
            txt_id_so_r.setText(sistema.id.toString())
            txt_nombre.setText(sistema.nombre)
            txt_version.setText(sistema.apellido)
            txt_fecha.setText(sistema.fechaNacimiento)
            txt_peso.setText(sistema.NumeroMedallas)
            esnuevo = false
        }



        boton_registrar_so.setOnClickListener {
            if(esnuevo){
                crearActualizarSO(true)
            }else{
                crearActualizarSO(false)
            }
        }

        boton_cancelar_reg_so.setOnClickListener {
            irInicio()
        }
    }

    fun crearActualizarSO(es_nuevo:Boolean){

        val id = txt_id_so_r.text.toString()
        val nombre = txt_nombre.text.toString()
        val version = txt_version.text.toString()
        val fecha = txt_fecha.text.toString()
        val peso = txt_peso.text.toString()
        val so:SistemaOperativo
        if(es_nuevo){
            so = SistemaOperativo(id=null,nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso)
        }else{
            so = SistemaOperativo(id=id.toInt(),nombre = nombre,version =  version,fechaLanzamiento =  fecha,peso_gigas =  peso)
        }

        //Crear objeto
        val parametros = listOf("nombrePokemon" to so.nombre, "poderEspecialUno" to so.version,
                "poderEspecialDos" to so.fechaLanzamiento, "fechaCaptura" to so.peso_gigas)
        Log.i("htpp",parametros.toString())
        var direccion = ""
        if(es_nuevo){
            direccion = "http://$ip:80/sistemas/api/"
            val url = direccion
                    .httpPost(parametros)
                    .responseString { request, response, result ->
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                Log.i("http-p", ex.toString())
                                mensaje(this,"error","Datos no validos")

                            }
                            is Result.Success -> run {
                                val data = result.get()
                                Log.i("http-p", data)
                                mensaje(this,"Aceptado","Datos validos, espere...")
                                cargarDatosSO(direccion, ::irlistarSo)
                            }
                        }
                    }
        }else{
            direccion = "http://$ip:80/sistemas/api/${so.id}/update"
            val url = direccion
                    .httpPut(parametros)
                    .responseString { request, response, result ->
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                Log.i("http-p", ex.toString())
                                mensaje(this,"error","Datos no validos")

                            }
                            is Result.Success -> run {
                                val data = result.get()
                                Log.i("http-p", data)
                                mensaje(this,"Aceptado","Datos validos, espere...")
                                val redire = "http://$ip:80/sistemas/api/"
                                cargarDatosSO(redire, ::irlistarSo)
                            }
                        }
                    }
        }
        Log.i("http",direccion)

    }

    fun irlistarSo(){
        val intent = Intent(
                this,
                ListarActivity::class.java
        )
        startActivity(intent)
    }

    fun irInicio(){
        finish()
        val intent = Intent(
                this,
                MainActivity::class.java
        )
        startActivity(intent)
    }

}
