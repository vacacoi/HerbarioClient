package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import jq.vc.uniquindio.co.herbarioclient.Fragmentos.ListaPlantasFragment
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas

import kotlinx.android.synthetic.main.activity_lista_plantas.*

class ListaPlantasActivity : AppCompatActivity(), ListaPlantasFragment.OnPlantaSeleccionadoListener {


    override fun onPlantaSeleccionado(pos: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    var listaPlantas: ArrayList<ListaPlantas> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_plantas)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        listaPlantas = ArrayList()
        listaPlantas.add(ListaPlantas("Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor"))
        listaPlantas.add(ListaPlantas("Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1"))
        listaPlantas.add(ListaPlantas("Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor"))
        listaPlantas.add(ListaPlantas("Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1"))
        listaPlantas.add(ListaPlantas("Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor", "Flor"))
        listaPlantas.add(ListaPlantas("Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1", "Flor1"))

        val fragmentLista = supportFragmentManager.findFragmentById(R.id.fragmentoListaPlantas) as ListaPlantasFragment
        fragmentLista.listaPlantas = listaPlantas

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }
}
