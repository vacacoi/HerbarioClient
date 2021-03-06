package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import jq.vc.uniquindio.co.herbarioclient.Fragmentos.ListaPlantasFragment
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas

import kotlinx.android.synthetic.main.activity_lista_plantas.*

class ListaPlantasActivity : AppCompatActivity(), ListaPlantasFragment.OnPlantaSeleccionadoListener {


    var listaPlantas: ArrayList<ListaPlantas> = ArrayList()
    lateinit var managerFireBase: ManagerFireBase


    override fun onPlantaSeleccionado(pos: Int) {

      // listaPlantas[pos]

    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_plantas)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        managerFireBase = ManagerFireBase.managerInstance



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}
