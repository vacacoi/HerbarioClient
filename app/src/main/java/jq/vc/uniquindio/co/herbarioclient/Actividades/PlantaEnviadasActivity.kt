package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import jq.vc.uniquindio.co.herbarioclient.Fragmentos.PlantasEnviadasFragment
import jq.vc.uniquindio.co.herbarioclient.R

import kotlinx.android.synthetic.main.activity_planta_enviadas.*

class PlantaEnviadasActivity : AppCompatActivity(),PlantasEnviadasFragment.OnPlantaSeleccionadoListener {
    override fun onPlantaSeleccionado(pos: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_planta_enviadas)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

}
