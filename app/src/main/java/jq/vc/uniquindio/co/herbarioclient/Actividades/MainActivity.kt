package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.Sesion
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var textView: TextView?=null
    var sesion:Sesion?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        //textView = findViewById<TextView>(R.id.titulo_principal)

        sesion = Sesion(this)
        if(!sesion!!.getusename().equals("")){
            Log.d("Apenas","llegue="+sesion!!.getusename())
            lanzarActividadSession()
            finish()
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {

            R.id.lista_plantas -> {
                val intent = Intent(this,ListaPlantasActivity::class.java)
                intent.putExtra("Lista plantas","1")
                startActivity(intent)
            }
            R.id.inicia_sesion -> {
               val intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("Iniciar Sesion","1")
                startActivity(intent)

            }
            R.id.registro -> {
                val intent = Intent(this,RegistroActivity::class.java)
                intent.putExtra("Iniciar Sesion","1")
                startActivity(intent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    /**
     * Si el usuario no ha cerrado sesión, carga la actvidad principal
     */
    fun lanzarActividadSession(){
        val intent = Intent(this,ActivityLogueado::class.java)
        intent.putExtra("Iniciar Sesion","1")
        startActivity(intent)
    }

}

