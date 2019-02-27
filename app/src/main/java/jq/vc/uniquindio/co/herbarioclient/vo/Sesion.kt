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

    fun setTelefono(telefono: String){
        prefs.edit().putString("telefono", telefono).commit()
    }
    fun getTelefono():String{
        return prefs.getString("telefono", "")
    }

    fun setProfesion(profesion: String){
        prefs.edit().putString("profesion", profesion).commit()
    }
    fun getProfesion():String{
        return prefs.getString("profesion", "")
    }

    fun setKey(key: String){
        prefs.edit().putString("key", key).commit()
    }
    fun getKey():String{
        return prefs.getString("key", "")
    }

    fun setTipo(tipo: String){
        prefs.edit().putString("tipo", tipo).commit()
    }
    fun getTipo():String{
        return prefs.getString("tipo", "")
    }

}