package com.example.pato_.exameniibrb

import android.os.Parcel
import android.os.Parcelable

// PARA LA API
class Entrenador(
        var id:Int?,
        var nombre: String,
        var apellido: String,
        var fechaNacimiento: String,
        var numeroMedallas: Int,
        var campeonActual: Boolean){}

class Pokemon(
    var id:Int?,
    var nombrePokemon: String,
    var poderEspecialUno: String,
    var poderEspecialDos: String,
    var fechaCaptura: String,
    var nivel:Int,
    var codigo_barras:String,
    var entrenador: Int?
){}


class EntrenadorSe(var id:Int?,
                   var nombre: String,
                   var apellido: String,
                   var fechaNacimiento: String,
                   var NumeroMedallas: Int,
                   var campeonActual: Boolean):Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(fechaNacimiento)
        parcel.writeInt(NumeroMedallas)
        parcel.writeBooleanArray(campeonActual)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntrenadorSe> {
        override fun createFromParcel(parcel: Parcel): EntrenadorSe {
            return EntrenadorSe(parcel)
        }

        override fun newArray(size: Int): Array<EntrenadorSe?> {
            return arrayOfNulls(size)
        }
    }
}

class PokemonSe(
    var id:Int?,
    var nombrePokemon: String,
    var poderEspecialUno: String,
    var poderEspecialDos: String,
    var fechaCaptura: String,
    var nivel:Int,
    var codigo_barras:String,
    var entrenador: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombrePokemon)
        parcel.writeString(poderEspecialUno)
        parcel.writeString(poderEspecialDos)
        parcel.writeString(fechaCaptura)
        parcel.writeInt(nivel)
        parcel.writeString(codigo_barras)
        parcel.writeInt(entrenador)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonSe> {
        override fun createFromParcel(parcel: Parcel): PokemonSe {
            return PokemonSe(parcel)
        }

        override fun newArray(size: Int): Array<PokemonSe?> {
            return arrayOfNulls(size)
        }
    }
}