package jq.vc.uniquindio.co.herbarioclient.vo

import android.content.Context
import android.preference.PreferenceManager
import android.content.SharedPreferences



class Sesion {

    private var prefs: SharedPreferences

    constructor(cntx: Context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx)
    }

    fun setusename(usename: String) {
        prefs.edit().putString("usename", usename).commit()
    }

    fun getusename(): String {
        return prefs.getString("usename", "")
    }

    fun setNombre(nombre : String){
        prefs.edit().putString("nombre", nombre).commit()
    }

    fun setApellido(apellido : String){
        prefs.edit().putString("apellido", apellido).commit()
    }

    fun getNombre():String{
        return prefs.getString("nombre", "")
    }

    fun getApellido():String{
        return prefs.getString("apellido", "")
    }

    fun setUrlFoto(url: String){
        prefs.edit().putString("url", url).commit()
    }
    fun getUrlFoto():String{
        return prefs.getString("url", "")
    }

}