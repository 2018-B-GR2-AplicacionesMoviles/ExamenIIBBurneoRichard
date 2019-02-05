package com.example.pato_.exameniibrb

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_listar_entrenadoractivity.*
import android.support.v7.widget.PopupMenu
import android.widget.Button
import com.github.kittinunf.result.Result
import android.content.Intent
import com.beust.klaxon.Klaxon
import com.example.andres.examenapp2.BDD.Companion.sistemasOperativos
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet


class ListarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_soactivity)

        //BDD.crearMas()

        val layoutManager = LinearLayoutManager(this)
        val rv = rv_so
        val adaptador = SistemaOpAdaptador(BDD.entrenador, this, rv)

        rv_so.layoutManager = layoutManager
        rv_so.itemAnimator = DefaultItemAnimator()
        rv_so.adapter = adaptador

        adaptador.notifyDataSetChanged()

    }


    fun refrescar(){
        finish()
        val direccion = "http://${BDD.ip}:80/sistemas/api/"
        Log.i("http",direccion)
        cargarDatosSO(direccion,fun(){})
        startActivity(getIntent());
    }

    fun compartir(contenido:String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, contenido)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }


    fun irActualizar(entrenador: EntrenadorSe){
        val intentActividadIntent = Intent(
                this,
                CrearEntrenadorActivity::class.java
        )

        intentActividadIntent.putExtra("sistema", entrenador)
        startActivity(intentActividadIntent)

    }

    fun irAlistarHijos(entrenador: EntrenadorSe){

        "http://${BDD.ip}:80/sistemas/api/app/?so=${entrenador.id}".httpGet().responseString{ request, response, result ->
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.i("http", ex.toString())
                }
                is Result.Success -> {
                    val data = result.get()
                    BDD.pokemones.clear()
                    val wordDict = Klaxon().parseArray<Pokemon>(data)
                    Log.i("http", "Datos: ${wordDict.toString()}")
                    if (wordDict != null) {
                        for ( item in wordDict.iterator()){
                            BDD.pokemones.add(item)
                        }
                    }

                    finish()
                    val intentActividadIntent = Intent(
                            this,
                            ListarPokemonesActivity::class.java
                    )

                    intentActividadIntent.putExtra("sistema", entrenador)
                    startActivity(intentActividadIntent)
                }
            }
        }
    }
}




class SistemaOpAdaptador(private val listaSistemaOperativos: List<SistemaOperativo>,
                         private val contexto: ListarActivity,
                         private val recyclerView: RecyclerView) :
        RecyclerView.Adapter<SistemaOpAdaptador.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nombreTextView: TextView
        var versionTextView: TextView
        var idSOTextView: TextView
        var opciones:Button

        init {
            nombreTextView = view.findViewById(R.id.txt_nombre_so) as TextView
            versionTextView = view.findViewById(R.id.txt_version_so) as TextView
            idSOTextView = view.findViewById(R.id.txt_so_id) as TextView
            opciones = view.findViewById(R.id.btn_opciones) as Button




            val layout = view.findViewById(R.id.relative_layout_so) as RelativeLayout

            layout
                    .setOnClickListener {
                        val nombreActual = it.findViewById(R.id.txt_nombre_so) as TextView

                        Log.i("recycler-view",
                                "El nombrePokemon actual es: ${nombreActual.text}")

                    }



        }
    }

    // Definimos el layout
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int): MyViewHolder {

        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                        R.layout.recycler_view_so_item,
                        parent,
                        false
                )

        return MyViewHolder(itemView)
    }

    // Llenamos los datos del layout
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sistema = listaSistemaOperativos[position]

        holder.nombreTextView.setText(sistema.nombre)
        holder.versionTextView.setText(sistema.version)
        holder.idSOTextView.setText(sistema.id.toString())
        holder.opciones.setOnClickListener {
            val popup = PopupMenu(contexto, holder.idSOTextView)
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.getItemId()) {
                    R.id.eliminar_so ->{
                        //handle menu1 click
                        mensaje_dialogo(contexto,"Eliminar el SO?",
                                fun (){
                                    val id = holder.idSOTextView.text.toString()
                                    Log.i("Eliminar SO->",id)

                                    val direccion = "http://${BDD.ip}:80/sistemas/api/"
                                    Log.i("http",direccion)
                                    val parametros = listOf("nombrePokemon" to id)
                                    val url = "http://${BDD.ip}:80/sistemas/api/$id/delete"
                                            .httpDelete(parametros)
                                            .responseString { request, response, result ->
                                                when (result) {
                                                    is Result.Failure -> {
                                                        val ex = result.getException()
                                                        Log.i("http-p", ex.toString())
                                                        mensaje(contexto,"error","Datos no validos")

                                                    }
                                                    is Result.Success -> run {
                                                        val data = result.get()
                                                        Log.i("http-p", data)
                                                        mensaje(contexto,"Aceptado","Datos validos, espere...")
                                                        contexto.refrescar()
                                                    }
                                                }
                                            }




                                }
                        )



                        true
                    }

                    R.id.editar_so ->{
                        val id = holder.idSOTextView.text.toString()
                        mensaje_dialogo(contexto,"Desea editar el SO?",

                                fun(){
                                    val so = sistemasOperativos.filter { it.id==id.toInt() }[0]
                                    Log.i("Actualizar SO->",so.fechaLanzamiento)
                                    val soSerializado = EntrenadorSe(
                                            id.toInt(),
                                            nombre = so.nombre,
                                            apellido = so.version,
                                            fechaNacimiento = so.fechaLanzamiento,
                                            NumeroMedallas = so.peso_gigas
                                    )
                                    contexto.irActualizar(soSerializado)
                                }

                                )

                        //handle menu2 click
                        true
                    }

                    R.id.compartir_so ->{
                        val nombre = holder.nombreTextView.text.toString()
                        contexto.compartir(nombre)
                        //handle menu3 click
                        true
                    }

                    R.id.hijos_so ->{
                        var direccion = ""
                        val id = holder.idSOTextView.text.toString()
                        val so = sistemasOperativos.filter { it.id==id.toInt() }[0]
                        Log.i("Listar SO->",so.fechaLanzamiento)
                        val soSerializado = EntrenadorSe(
                                id.toInt(),
                                nombre = so.nombre,
                                apellido = so.version,
                                fechaNacimiento = so.fechaLanzamiento,
                                NumeroMedallas = so.peso_gigas
                        )
                        contexto.irAlistarHijos(soSerializado)
                        true
                    }


                    else -> false
                }
            }
            //displaying the popup
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return listaSistemaOperativos.size
    }


}










