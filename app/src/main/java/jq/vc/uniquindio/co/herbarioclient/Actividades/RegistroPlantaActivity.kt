package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.View
import jq.vc.uniquindio.co.herbarioclient.R

import kotlinx.android.synthetic.main.activity_registro_planta.*
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.io.File
import kotlin.random.Random
import android.content.pm.PackageManager
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.support.v4.content.ContextCompat
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.annotation.NonNull
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.support.v4.app.ActivityCompat
import android.content.DialogInterface
import android.annotation.TargetApi
import android.support.v7.app.AlertDialog
import android.content.Context;
import android.content.res.Configuration
import android.os.Environment.getExternalStorageDirectory

import android.support.v4.content.FileProvider
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Color
import android.os.*
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas


class RegistroPlantaActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200

    var fotoPlanta: ImageView? = null
    var nombre: String? = null
    lateinit var managerFireBase: ManagerFireBase
    var listaPlantas: ArrayList<ListaPlantas> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_planta)
        managerFireBase = ManagerFireBase.managerInstance


        fotoPlanta = findViewById(R.id.foto_planta)
        Log.d("EstaAqui", "Hola")

        btn_tomar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if (!checkPermission()) {
                    tomarFoto()
                } else {
                    if (checkPermission()) {
                        requestPermissionAndContinue();
                    } else {
                        tomarFoto()
                    }
                }
            }
        })

        btn_enviar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                listaPlantas = ArrayList()
                agregarPlanta(
                    ListaPlantas(
                        textNombreTDet.text.toString(),
                        textGeneroTDet.text.toString(),
                        textFamiliaTDet.text.toString(),
                        textSubFamiliaTDet.text.toString(),
                        textTribuTDet.text.toString(),
                        textEspecieTDet.text.toString(),
                        textDetalleTDet.text.toString(),
                        "Victor",
                        "I",
                        "null"
                    )
                )


            }
        })
    }


    /**
     * Función que permite agregar una planta a .
     * @param listaPlanta Lista de plantas a agregar
     */
    fun agregarPlanta(listaPlanta: ListaPlantas) {

        val ruta: String = getExternalStorageDirectory().getPath() + "/" + nombre
        var urlImagen:String?=null
        val rutaImagenDb: String = textGeneroTDet.text.toString() + "/" + textFamiliaTDet.text.toString() +
                "/" + textSubFamiliaTDet.text.toString() + "/" + textTribuTDet.text.toString() +
                "/" + textEspecieTDet.text.toString() + "/" + nombre
        var llaveImagen = managerFireBase.insetarConLLave(listaPlanta)

        managerFireBase.uploadImage(ruta, rutaImagenDb,llaveImagen)

        alertDialog()

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
            "El reporte de su planta ha sido cargado con éxito" +
                    ", ahora se encuentra a la espera de aprobación por parte " +
                    "del administrador"
        )

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Do something when user press the positive button
            finish()
            // Change the app background colo
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    /**
     * Recibe el resultado de la ejecución de la actividad.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap1 = BitmapFactory.decodeFile(getExternalStorageDirectory().getPath() + "/" + nombre)
        Log.d("Imagen es", "" + getExternalStorageDirectory().getPath() + "/" + nombre + "---" + bitmap1)

        fotoPlanta!!.setImageBitmap(bitmap1)


    }


    @SuppressLint("ResourceType")
    fun tomarFoto() {
        val rnds = (0..100).random()
        nombre = "planta" + rnds + ".jpg"

        val intento1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val foto = File(getExternalStorageDirectory().getPath() + "/" + nombre)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intento1.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(this, "jq.vc.uniquindio.co.herbarioclient.fileProvider", foto)
            intento1.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)

        } else {
            intento1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(foto.toString()))
        }

        startActivityForResult(intento1, 0)

    }


    /**
     * Función que permite saber si la aplicacion posee permisos de lectura y escritura
     * en almacenamiento interno.
     * @return
     */
    private fun checkPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * Método que aplica permisos de lectura y escitura en almacenamiento interno.
     */
    private fun requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
            ) {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle(getString(R.string.permission_necessary))
                alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_wrote_event)
                alertBuilder.setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@RegistroPlantaActivity,
                            arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE
                        )
                    })
                val alert = alertBuilder.create()
                alert.show()
                Log.e("", "permission denied, show dialog")
            } else {
                ActivityCompat.requestPermissions(
                    this@RegistroPlantaActivity,
                    arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        } else {
            tomarFoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.size > 0 && grantResults.size > 0) {

                var flag = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false
                    }
                }
                if (flag) {
                    tomarFoto()
                } else {
                    finish()
                }

            } else {
                finish()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}
