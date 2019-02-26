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
}