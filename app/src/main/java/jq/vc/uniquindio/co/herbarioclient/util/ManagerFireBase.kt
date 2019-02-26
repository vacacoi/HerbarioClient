package jq.vc.uniquindio.co.herbarioclient.util

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import java.io.File
import java.security.AccessControlContext
import android.support.annotation.NonNull
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import android.support.v4.os.HandlerCompat.postDelayed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.DataSnapshot
import jq.vc.uniquindio.co.herbarioclient.vo.Usuarios
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.app.ProgressDialog
import android.support.v7.app.AlertDialog
import com.google.firebase.auth.UserProfileChangeRequest
import jq.vc.uniquindio.co.herbarioclient.vo.Sesion


class ManagerFireBase private constructor() {
    private var database: FirebaseDatabase? = null
    private var dataRef: DatabaseReference? = null
    private var dataStore: StorageReference? = null
    private var dataAuth: FirebaseAuth? = null
    lateinit var listener: onActualizarAdaptador
    var sesion: Sesion? = null
    //hace referencia a la bd


    init {
        //para inicializar antes de que el constructor secundario se inicialice
        database = FirebaseDatabase.getInstance()
        dataRef = database!!.reference
        dataStore = FirebaseStorage.getInstance().reference
        dataAuth = FirebaseAuth.getInstance()

        //reference es la rama principal
    }

    companion object {
        // para que este codigo de adentro sea visible desde otras clases
        //el object es parecido al singleton


        private var instancia: ManagerFireBase? = null
        val managerInstance: ManagerFireBase
            get() {
                if (instancia == null) {
                    instancia = ManagerFireBase()
                }
                return instancia!!
            }
    }

    fun insertar(listaPlantas: ListaPlantas) {
        //AUTOGENERA LLAVE PRIMARIA
        dataRef!!.push().setValue(listaPlantas)

    }

    fun insetarConLLavePlanta(listaPlantas: ListaPlantas): String {

        dataRef!!.child(llave()).child("plantas").setValue(listaPlantas)
        return llave()
    }

    fun insetarConLLaveUsuario(usuarios: Usuarios): String {

        dataRef!!.child(llave()).child("usuarios").setValue(usuarios)
        return llave()
    }

    /**
     * Genera clave de acuerdo a la fecha y hora.
     */
    fun llave(): String {
        val date = Date()
        val hourdateFormat = SimpleDateFormat("HHmmssddMMyyyy")
        return hourdateFormat.format(date)
    }

    fun eliminar() {
    }

    fun actualizar() {
    }

    /**
     * Permite cargar una imagen a FireBase y actualizar la url en donde queda la imagen guardada.
     */
    fun uploadImage(file: String, rutaBD: String, llave: String, tipo: Int): String? {
        val file = Uri.fromFile(File(file))
        val riversRef = dataStore!!.child(rutaBD)
        var downloadUri: String? = null

        riversRef.putFile(file).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
            riversRef.getDownloadUrl().addOnSuccessListener(
                OnSuccessListener<Uri> { uri ->
                    Log.d("Holaa", "onSuccess: uri= $uri")
                    if (tipo == 1) {
                        dataRef!!.database.reference.child(llave).child("plantas").child("urlImagen")
                            .setValue(uri.toString())
                    } else if (tipo == 2) {
                        dataRef!!.database.reference.child(llave).child("usuarios").child("urlImagenPerfil")
                            .setValue(uri.toString())
                    }
                })
        })

        return downloadUri
    }


    /**
     * Método que permite consultar en tiempo real a firebase y traer los datos almacenados
     */
    fun escucharEventoFireBase(tipo: Int, context: Context) {

        sesion = Sesion(context)

        dataRef!!.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.v("ManagerFire", "onCancelled")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.v("ManagerFire", "onChildMoved")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                if (p0.child("plantas").exists() && tipo == 1) {
                    val listaPlantas = p0!!.child("plantas").getValue(ListaPlantas::class.java)!!
                    if (listaPlantas != null && listaPlantas!!.estado == "A") {
                        listener.actualizarAdaptador(listaPlantas)
                    }
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (p0.child("plantas").exists() && tipo == 1) {
                    val listaPlantas = p0.child("plantas").getValue(ListaPlantas::class.java)!!
                    if (listaPlantas.estado == "A") {
                        listener.actualizarAdaptador(listaPlantas)
                    }

                } else if (p0.child("usuarios").exists() && tipo == 2) {
                    val listaUsuario = p0.child("usuarios").getValue(Usuarios::class.java)!!

                    listener.cedredenciales(listaUsuario)

                } else if (p0.child("plantas").exists() && tipo == 3) {
                    val listaPlantas = p0.child("plantas").getValue(ListaPlantas::class.java)!!
                    if (sesion!!.getusename().equals(listaPlantas.email)) {
                        listener.actualizarAdaptador(listaPlantas)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    /*fun listaPlantas(): ArrayList<ListaPlantas> {


        Log.d("hola","="+dataRef!!.orderByKey().equalTo("plantas")+"----"+dataRef!!.orderByKey())
        var listaPlantas: ArrayList<ListaPlantas> = ArrayList()
        dataRef!!.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.getValue(ListaPlantas::class.java)
                for (snapshot in p0.getChildren()) {

                    if(snapshot.hasChildren()){
                        val listaplanta = snapshot.child("plantas").getValue(ListaPlantas::class.java)

                        Log.d(
                            "Nombre", listaplanta!!.urlImagen
                        )
                    }


                }


            }


        })


        return listaPlantas
    }*/

    interface onActualizarAdaptador {
        fun actualizarAdaptador(listaPlantas: ListaPlantas)
        fun cedredenciales(usuarios: Usuarios)
    }

    /**
     * Registrar email y contraseña
     */

    fun registroUsuario(
        email: String,
        pass: String,
        progressDialog: ProgressDialog,
        context: Context,
        ruta: String,
        rutaImagenDb: String,
        usuarios: Usuarios
    ) {


        dataAuth!!.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = dataAuth!!.getCurrentUser()
                    var llaveImagen = insetarConLLaveUsuario(usuarios)
                    uploadImage(ruta, rutaImagenDb, llaveImagen, 2)
                    progressDialog.dismiss()
                    alertDialog(context)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Hola", "createUserWithEmail:failure", task.exception)
                    progressDialog.dismiss()
                    Toast.makeText(
                        context,
                        "No se pudo registrar el correo electrónico o contraseña (mayor a 6 digitos)",
                        Toast.LENGTH_LONG
                    ).show()
                    return@OnCompleteListener
                }
            })


    }


    fun ingresar(email: String, password: String) {

    }


    /**
     * Función que permite abrir un dialogo donde se confirma la que la planta ha
     * sido agregada con exito
     */
    fun alertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)

        // Set the alert dialog title
        builder.setTitle("Información")

        // Display a message on alert dialog
        builder.setMessage(
            "Se ha registrado correctamente!!"
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
}