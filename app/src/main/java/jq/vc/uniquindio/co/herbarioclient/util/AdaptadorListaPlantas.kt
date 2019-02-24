package jq.vc.uniquindio.co.herbarioclient.util

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import jq.vc.uniquindio.co.herbarioclient.Actividades.DetallePlantasActivity
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import kotlinx.android.synthetic.main.resumen_lista_plantas.view.*


class AdaptadorListaPlantas (fragment: Fragment, var listaPlantas:ArrayList<ListaPlantas>,context:Context) :RecyclerView.Adapter<AdaptadorListaPlantas.ListaPlantasViewHolder>() {

    private lateinit var listener:OnClickAdaptadorListaPlantas

     var context = context



    init{
        try {
            listener= fragment as OnClickAdaptadorListaPlantas
        }catch (e:ClassCastException){

            Log.v("AdaptadorDeListaPlanta", "Error, implemente la interfaz...")
        }

    }

    inner class ListaPlantasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener{


        val nombre: TextView = itemView.txtNombre
        val genero: TextView = itemView.txtGenero
        val btnDetalle: Button = itemView.btn_detalle


        init {
            itemView.setOnClickListener(this)
        }


        fun bindListaPlantas(listaPlantas: ListaPlantas){
            nombre.text = listaPlantas.nombre
            genero.text = listaPlantas.genero



        }
        override fun onClick(v: View?) {
            Log.d("El valor es,","+"+v!!.id)
             Log.d("Planta", "Elemento $adapterPosition clickeado ${nombre.text}")
            listener.onClickPosition(adapterPosition)




        }




    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ListaPlantasViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.resumen_lista_plantas, p0, false)

        return ListaPlantasViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listaPlantas.size
    }

    override fun onBindViewHolder(p0: ListaPlantasViewHolder, p1: Int) {

        p0.bindListaPlantas(listaPlantas.get(p1))
        p0.btnDetalle.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mostrarDetallePlanta(p1)

            }

        })
    }

    interface OnClickAdaptadorListaPlantas {
        fun onClickPosition(pos: Int)
    }

    /**
     * Función que permite lanzar actividad de detalle PALNTAS
     */

    fun mostrarDetallePlanta(p1: Int) {

        Log.v("Llega hasta aqui", "Hasta aqui")

        val intent = Intent(context, DetallePlantasActivity::class.java)
        intent.putExtra("lista",listaPlantas)
        intent.putExtra("p0",p1)
        context.startActivity(intent)

    }


}