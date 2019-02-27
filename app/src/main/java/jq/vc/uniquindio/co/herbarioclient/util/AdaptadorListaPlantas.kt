package jq.vc.uniquindio.co.herbarioclient.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import jq.vc.uniquindio.co.herbarioclient.Actividades.DetallePlantasActivity
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import kotlinx.android.synthetic.main.resumen_lista_plantas.view.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


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
        private var fotoPlanta: ImageView = itemView.imgPlanta


        init {
            itemView.setOnClickListener(this)
        }


        fun bindListaPlantas(listaPlantas: ListaPlantas){
            nombre.text = listaPlantas.nombre
            genero.text = listaPlantas.genero
            GetImageToURL().execute(listaPlantas.urlImagen)



        }
        override fun onClick(v: View?) {
            Log.d("El valor es,","+"+v!!.id)
             Log.d("Planta", "Elemento $adapterPosition clickeado ${nombre.text}")
            listener.onClickPosition(adapterPosition)
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
                fotoPlanta!!.setImageBitmap(myBitMap)
            }
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

        var listaPlanta: ArrayList<ListaPlantas> = ArrayList()
        listaPlanta = ArrayList()

        listaPlanta.add(
            ListaPlantas(listaPlantas[p1].nombre,listaPlantas[p1].genero,listaPlantas[p1].familia,listaPlantas[p1].subfamilia,
                listaPlantas[p1].tribu,listaPlantas[p1].especie,listaPlantas[p1].detalle,listaPlantas[p1].autor,
                listaPlantas[p1].estado,listaPlantas[p1].urlImagen,listaPlantas[p1].email,listaPlantas[p1].key,listaPlantas[p1].fecha))

        val intent = Intent(context, DetallePlantasActivity::class.java)
        intent.putExtra("lista",listaPlanta)
        intent.putExtra("p0",0)
        context.startActivity(intent)

    }


}