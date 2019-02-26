package jq.vc.uniquindio.co.herbarioclient.Fragmentos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity

import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.util.AdaptadorPlantasEnviadas
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import jq.vc.uniquindio.co.herbarioclient.vo.Usuarios
import kotlinx.android.synthetic.main.fragment_plantas_enviadas.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlantasEnviadasFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PlantasEnviadasFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlantasEnviadasFragment : Fragment(),AdaptadorPlantasEnviadas.OnClickAdaptadorPlantasEnviadas,ManagerFireBase.onActualizarAdaptador {

    override fun cedredenciales(usuarios: Usuarios) {

    }


    override fun actualizarAdaptador(listaPlantas: ListaPlantas) {
        agregarPlantas(listaPlantas)
    }


    private lateinit var listener: OnPlantaSeleccionadoListener
    var listaPlantas: ArrayList<ListaPlantas> = ArrayList()
    var adaptador: AdaptadorPlantasEnviadas? = null
    lateinit var managerFireBase: ManagerFireBase


    interface OnPlantaSeleccionadoListener {
        fun onPlantaSeleccionado(pos: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.d("Oprime Lista plantas", "=");


        return inflater.inflate(R.layout.fragment_plantas_enviadas, container, false)
    }

    override fun onPause() {
        super.onPause()
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Activity) {

            try {
                listener = context as OnPlantaSeleccionadoListener
            } catch (e: ClassCastException) {
                throw ClassCastException("${activity.toString()} debe implementar la interfaz OnListaSeleccionadoListener")
                // Log.v("ListaDePokemonFragment", "Error: ${e.message}")

            }


        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        managerFireBase = ManagerFireBase.managerInstance
        managerFireBase.listener = this
        managerFireBase.escucharEventoFireBase(3, context!!)//Llama al método escucharEventoFirebase, dónde se envía por parámetro 1 los cual indica mostrar lista de plantas

        adaptador = AdaptadorPlantasEnviadas(this,listaPlantas,activity!!.baseContext)
        plantasEnviadas_view.adapter = this.adaptador
        plantasEnviadas_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        setHasOptionsMenu(true)
    }



    override fun onClickPosition(pos: Int) {
        listener!!.onPlantaSeleccionado(pos)
    }

    fun agregarPlantas(listaPlanta: ListaPlantas) {
        listaPlantas.add(listaPlanta)
        adaptador!!.notifyItemChanged(0)
    }

}
