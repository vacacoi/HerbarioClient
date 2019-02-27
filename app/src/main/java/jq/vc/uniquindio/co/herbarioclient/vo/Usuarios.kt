package jq.vc.uniquindio.co.herbarioclient.vo

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


class Usuarios() : Parcelable, Serializable {

    var nombre: String? = null
    var apellido: String? = null
    var correo: String? = null
    var pass: String? = null
    var telefono: String? = null
    var profesion: String? = null
    var urlImagenPerfil: String? = null
    var key: String? = null
    var tipo: String? = null

    constructor(parcel: Parcel) : this() {
        nombre = parcel.readString()
        apellido = parcel.readString()
        correo = parcel.readString()
        telefono = parcel.readString()
        profesion = parcel.readString()
        urlImagenPerfil = parcel.readString()
        key = parcel.readString()
        tipo = parcel.readString()
    }


    constructor(
        nombre: String?,
        apellido: String?,
        correo: String?,
        pass: String?,
        telefono: String?,
        profesion: String?,
        urlImagenPerfil: String?,
        key: String?,
        tipo: String?
    ) : this() {

        this.nombre = nombre
        this.apellido = apellido
        this.correo = correo
        this.pass = pass
        this.telefono = telefono
        this.profesion = profesion
        this.urlImagenPerfil = urlImagenPerfil
        this.key = key
        this.tipo = tipo

    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(correo)
        parcel.writeString(pass)
        parcel.writeString(telefono)
        parcel.writeString(profesion)
        parcel.writeString(urlImagenPerfil)
        parcel.writeString(key)
        parcel.writeString(tipo)
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