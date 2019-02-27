package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import jq.vc.uniquindio.co.herbarioclient.vo.Sesion
import jq.vc.uniquindio.co.herbarioclient.vo.Usuarios

import kotlinx.android.synthetic.main.activity_datos_usuario.*
import kotlinx.android.synthetic.main.content_datos_usuario.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

class DatosUsuarioActivity : AppCompatActivity(), ManagerFireBase.onActualizarAdaptador {


    lateinit var managerFireBase: ManagerFireBase
    var listaUsuarios: ArrayList<Usuarios> = ArrayList()
    var sesion: Sesion? = null
    var time: Timer = Timer()

    //Start this timer when you create you task


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_usuario)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        val timer = Timer("schedule", true);

// schedule a single event
        

        managerFireBase = ManagerFireBase.managerInstance
        managerFireBase.listener = this

        sesion = Sesion(this)


        txtDatosCorreo.text = sesion!!.getusename()
        txtNombreDatos.setText(sesion!!.getNombre())
        txtDatosApellido.setText(sesion!!.getApellido())
        txtDatoTelefono.setText(sesion!!.getTelefono())
        txtDatosProfesion.setText(sesion!!.getProfesion())


        btn_modificar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                managerFireBase.updateUser(txtNombreDatos.text.toString(),txtDatosApellido.text.toString(),
                    txtDatoTelefono.text.toString(),
                    txtDatosProfesion.text.toString(),
                    sesion!!.getKey())
                sesion!!.setNombre(txtNombreDatos.text.toString())
                sesion!!.setApellido(txtDatosApellido.text.toString())
                sesion!!.setTelefono(txtDatoTelefono.text.toString())
                sesion!!.setProfesion(txtDatosProfesion.text.toString())
                alertDialog()

            }

        })


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun actualizarAdaptador(listaPlantas: ListaPlantas) {

    }

    override fun cedredenciales(usuarios: Usuarios) {
        listaUsuarios(usuarios)
    }

    fun listaUsuarios(usuarios: Usuarios) {
        listaUsuarios.add(usuarios)

    }

    /**
     * Función que permite abrir un dialogo donde se confirma la que la planta ha
     * sido agregada con exito
     */
    fun alertDialog() {
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle("Información")

        // Display a message on alert dialog
        builder.setMessage(
            "Se ha actualizado información correctamente!!"
        )

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Aceptar") { dialog, which ->
            val intent = Intent(this,ActivityLogueado::class.java)
            intent.putExtra("Iniciar Sesion","1")
            startActivity(intent)
            finish()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}
