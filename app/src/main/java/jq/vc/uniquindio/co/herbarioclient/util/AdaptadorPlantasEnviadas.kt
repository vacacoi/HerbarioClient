package jq.vc.uniquindio.co.herbarioclient.util

import android.content.Context
import android.provider.Settings.Global.getString
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import kotlinx.android.synthetic.main.resumen_lista_plantas.view.*
import kotlinx.android.synthetic.main.resumen_plantas_enviadas.view.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class AdaptadorPlantasEnviadas(fragment: Fragment, var listaPlantas: ArrayList<ListaPlantas>, context: Context) :
    RecyclerView.Adapter<AdaptadorPlantasEnviadas.PlantasEnviadasViewHolder>() {


    private lateinit var listener: OnClickAdaptadorPlantasEnviadas
    var res:Resources?=null



    var context = context


    init {
        try {
            listener = fragment as OnClickAdaptadorPlantasEnviadas
        } catch (e: ClassCastException) {

            Log.v("AdaptadorDeListaPlanta", "Error, implemente la interfaz...")
        }

    }

    inner class PlantasEnviadasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


        val nombre: TextView = itemView.textNombreEnv
        val fecha: TextView = itemView.textfechaEnv
        val estado: TextView = itemView.textEstadoEnv
        private var fotoPlanta: ImageView = itemView.imgPlantaEnv


        init {
            itemView.setOnClickListener(this)
        }


        fun bindListaPlantas(listaPlantas: ListaPlantas) {
            res = Resources.getSystem()
            nombre.text = listaPlantas.nombre
            fecha.text = listaPlantas.genero
            GetImageToURL().execute(listaPlantas.urlImagen)

            if (listaPlantas.estado.equals("A")) {
                estado.text = "APROBADO"
            } else if (listaPlantas.estado.equals("I")) {
                estado.text = "PENDIENTE"
            }


        }

        override fun onClick(v: View?) {
            Log.d("El valor es,", "+" + v!!.id)
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

    override fun getItemCount(): Int {
        return listaPlantas.size
    }

    override fun onBindViewHolder(p0: PlantasEnviadasViewHolder, p1: Int) {

        p0.bindListaPlantas(listaPlantas.get(p1))

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlantasEnviadasViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.resumen_plantas_enviadas, p0, false)

        return PlantasEnviadasViewHolder(v)
    }


    interface OnClickAdaptadorPlantasEnviadas {
        fun onClickPosition(pos: Int)
    }


}