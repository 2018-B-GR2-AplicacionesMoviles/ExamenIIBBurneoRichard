package com.example.pato_.exameniibrb

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pato_.exameniibrb.BDD.Companion.ip
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boton_listar.setOnClickListener {
            val direccion = "http://$ip:80/sistemas/api/"
            Log.i("http",direccion)
            cargarDatosSO(direccion,::irActividadListarOS)
        }

        boton_crear_padre.setOnClickListener{
            irActividadCrearOS()
        }


    }

    fun irActividadCrearOS(){
        val intent = Intent(
                this,
                CrearEntrenadorActivity::class.java
        )
        startActivity(intent)
    }

    fun irActividadListarOS(){
        finish()
        val intent = Intent(
                this,
                ListarActivity::class.java
        )
        startActivity(intent)

    }
}
