package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.Sesion
import kotlinx.android.synthetic.main.activity_logueado.*
import kotlinx.android.synthetic.main.app_bar_activity_logueado.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ActivityLogueado : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var nombre: String? = null
    var apellido: String? = null
    var sesion: Sesion? = null
    private var fotoPerfil: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logueado)
        setSupportActionBar(toolbar_logueado)

        sesion = Sesion(this)

        nombre = sesion!!.getNombre()
        apellido = sesion!!.getApellido()
        if (!!sesion!!.getUrlFoto().equals(null)) {
            GetImageToURL().execute(sesion!!.getUrlFoto())
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout_logueado,
            toolbar_logueado,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout_logueado.addDrawerListener(toggle)
        toggle.syncState()

        textSubNombre.text = nombre + " " + apellido
        val navigationView = findViewById<View>(R.id.nav_view_logueados) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navNombre = headerView.findViewById(R.id.textView_nombre) as TextView
        fotoPerfil = headerView.findViewById(R.id.imageView_logueado) as ImageView
        navNombre.text = nombre + " " + apellido

        nav_view_logueados.setNavigationItemSelectedListener(this)


    }

    override fun onBackPressed() {
        if (drawer_layout_logueado.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_logueado.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_logueado, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings_logueado -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        Log.d("Llega hasta aqui", "asdasd")
        when (item.itemId) {
            R.id.lista_plantas_logueado -> {
                val intent = Intent(this, ListaPlantasActivity::class.java)
                intent.putExtra("lista_plantas", "1")
                startActivity(intent)
            }
            R.id.ingresar_planta -> {
                val intent = Intent(this, RegistroPlantaActivity::class.java)
                intent.putExtra("registro_plantas", "1")
                startActivity(intent)
            }
            R.id.plantas_ingresadas -> {
                val intent = Intent(this, PlantaEnviadasActivity::class.java)
                intent.putExtra("plantas_enviadas", "1")
                startActivity(intent)
            }
            R.id.datos_cuenta -> {


            }
            R.id.salir_login -> {
                sesion!!.setusename("")
                sesion!!.setNombre("")
                sesion!!.setApellido("")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

        drawer_layout_logueado.closeDrawer(GravityCompat.START)
        return true
    }

    private inner class GetImageToURL : AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg params: String): Bitmap? {
            try {
                val url = URL(params[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input = connection.getInputStream()
                return BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                // Log exception
                return null
            }

        }

        override fun onPostExecute(myBitMap: Bitmap) {
            fotoPerfil!!.setImageBitmap(myBitMap)
        }
    }
}
