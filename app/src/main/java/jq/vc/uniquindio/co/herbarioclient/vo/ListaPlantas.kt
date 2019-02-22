package jq.vc.uniquindio.co.herbarioclient.vo

import android.os.Parcel
import android.os.Parcelable

class ListaPlantas() : Parcelable {

    var nombre: String? = null
    var genero: String? = null
    var familia: String? = null
    var subfamilia: String? = null
    var tribu: String? = null
    var especie: String? = null
    var detalle: String? = null
    var autor: String? = null
    var estado: String? = null

    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        genero = parcel.readString()
        familia = parcel.readString()
        subfamilia = parcel.readString()
        tribu = parcel.readString()
        especie = parcel.readString()
        detalle = parcel.readString()
        autor = parcel.readString()

    }


    constructor(nombre: String?,genero: String?,familia: String?,subfamilia: String?, tribu: String?, especie: String?,
                detalle: String?,autor: String?) : this() {

        this.nombre = nombre
        this.genero = genero
        this.familia = familia
        this.subfamilia = subfamilia
        this.tribu = tribu
        this.especie = especie
        this.detalle = detalle
        this.autor = autor


    }

    constructor(nombre: String?,genero: String?,familia: String?,subfamilia: String?, tribu: String?, especie: String?,
                detalle: String?,autor: String?, estado:String?) : this() {

        this.nombre = nombre
        this.genero = genero
        this.familia = familia
        this.subfamilia = subfamilia
        this.tribu = tribu
        this.especie = especie
        this.detalle = detalle
        this.autor = autor
        this.estado = estado


    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(nombre)
        parcel.writeString(genero)
        parcel.writeString(familia)
        parcel.writeString(subfamilia)
        parcel.writeString(tribu)
        parcel.writeString(especie)
        parcel.writeString(detalle)
        parcel.writeString(autor)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListaPlantas> {
        override fun createFromParcel(parcel: Parcel): ListaPlantas {
            return ListaPlantas(parcel)
        }

        override fun newArray(size: Int): Array<ListaPlantas?> {
            return arrayOfNulls(size)
        }
    }


}