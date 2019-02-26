package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.ImageView
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import jq.vc.uniquindio.co.herbarioclient.vo.Usuarios

import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro_planta.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistroActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200
    var fotoPlanta: ImageView? = null
    var nombre: String? = null
    var email: String? = null
    var pass: String? = null
    lateinit var managerFireBase: ManagerFireBase
    var usuarios: ArrayList<Usuarios> = ArrayList()
    var progressDialog: ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        managerFireBase = ManagerFireBase.managerInstance
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        progressDialog = ProgressDialog(this)


        fotoPlanta = findViewById(R.id.foto_perfil)


        btn_tomarFoto.setOnClickListener(object : View.OnClickListener {
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

        btn_registrar.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {


                email =  textCorreoTRes.text.toString()
                pass =   textPassTRes.text.toString()
                usuarios = ArrayList()

                progressDialog!!.setMessage("Guardando datos")
                progressDialog!!.show()
                agregarUsuario(
                    Usuarios(
                        textNombreTRes.text.toString(),
                        textApellidoTRes.text.toString(),
                        textCorreoTRes.text.toString().toLowerCase(),
                        null,
                        textTelefonoTRes.text.toString(),
                        textProfesionTRes.text.toString(),
                        "null"
                    )
                )




            }
        })

    }


    /**
     * Función que permite agregar usuarios a firebase .
     * @param usuarios Lista de usuarios a agregar
     */
    fun agregarUsuario(usuarios: Usuarios) {

        val ruta: String = Environment.getExternalStorageDirectory().getPath() + "/" + nombre
        val rutaImagenDb: String = "fotoUsuarios" + "/" + nombre
        managerFireBase.registroUsuario(email!!,pass!!,progressDialog!!,this,ruta,rutaImagenDb,usuarios)

    }




    fun errorDialog() {
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle("Error")

        // Display a message on alert dialog
        builder.setMessage(
            "No se pudo registrar el correo electrónico o contraseña (mayor a 6 digitos)"
        )

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Aceptar") { dialog, which ->
            // Do something when user press the positive button
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
        val bitmap1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + "/" + nombre)
        Log.d("Imagen es", "" + Environment.getExternalStorageDirectory().getPath() + "/" + nombre + "---" + bitmap1)

        fotoPlanta!!.setImageBitmap(bitmap1)


    }


    @SuppressLint("ResourceType")
    fun tomarFoto() {
        val rnds = (0..100).random()
        nombre = llave()+ ".jpg"

        val intento1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val foto = File(Environment.getExternalStorageDirectory().getPath() + "/" + nombre)
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    }

    /**
     * Método que aplica permisos de lectura y escitura en almacenamiento interno.
     */
    private fun requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle(getString(R.string.permission_necessary))
                alertBuilder.setMessage(R.string.storage_permission_is_encessary_to_wrote_event)
                alertBuilder.setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this@RegistroActivity,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            PERMISSION_REQUEST_CODE
                        )
                    })
                val alert = alertBuilder.create()
                alert.show()
                Log.e("", "permission denied, show dialog")
            } else {
                ActivityCompat.requestPermissions(
                    this@RegistroActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    /**
     * Genera clave de acuerdo a la fecha y hora.
     */
    fun llave(): String {
        val date = Date()
        val hourdateFormat = SimpleDateFormat("HHmmssddMMyyyy")
        return hourdateFormat.format(date)
    }
}



