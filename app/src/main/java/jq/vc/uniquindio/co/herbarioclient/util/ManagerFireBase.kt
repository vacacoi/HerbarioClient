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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.DataSnapshot


class ManagerFireBase private constructor() {
    private var database: FirebaseDatabase? = null
    private var dataRef: DatabaseReference? = null
    private var dataStore: StorageReference? = null
    lateinit var listener: onActualizarAdaptador
    //hace referencia a la bd


    init {
        //para inicializar antes de que el constructor secundario se inicialice
        database = FirebaseDatabase.getInstance()
        dataRef = database!!.reference
        dataStore = FirebaseStorage.getInstance().reference
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

    fun insetarConLLave(listaPlantas: ListaPlantas): String {

        dataRef!!.child(llave()).child("plantas").setValue(listaPlantas)
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
    fun uploadImage(file: String, rutaBD: String, llave: String): String? {
        val file = Uri.fromFile(File(file))
        val riversRef = dataStore!!.child(rutaBD)
        var downloadUri: String? = null

        riversRef.putFile(file).addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> {
            riversRef.getDownloadUrl().addOnSuccessListener(
                OnSuccessListener<Uri> { uri ->
                    Log.d("Holaa", "onSuccess: uri= $uri")

                    dataRef!!.database.reference.child(llave).child("plantas").child("urlImagen").setValue(uri.toString())
                })
        })

        escucharEventoFireBase()
        return downloadUri
    }


    fun escucharEventoFireBase() {
        dataRef!!.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.v("ManagerFire", "onCancelled")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                Log.v("ManagerFire", "onChildMoved")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.v("ManagerFire", "onChildChanged")
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val listaPlantas = p0!!.getValue(ListaPlantas::class.java)!!
                listaPlantas.autor = p0!!.key
                Log.v("ManagerFire", "onChildAdded" + p0!!.key)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.v("ManagerFire", "onChildRemoved")
            }
        })
    }

    fun listaPlantas(): ArrayList<ListaPlantas> {



        Log.d("hola","="+dataRef!!.orderByKey().equalTo("plantas")+"----"+dataRef!!.orderByKey())
        var listaPlantas: ArrayList<ListaPlantas> = ArrayList()
        dataRef!!.orderByKey().equalTo("plantas").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.getValue(ListaPlantas::class.java)
                for (snapshot in p0.getChildren()) {

                    val listaplanta = snapshot.getValue(ListaPlantas::class.java)
                    if (listaplanta != null) {
                        listener.actualizarAdaptador(listaplanta)
                        //listaPlantas.add(listaplanta)

                    }
                    Log.d(
                        "Nombre", listaplanta!!.urlImagen
                    )

                }


            }


        })


        return listaPlantas
    }

    interface onActualizarAdaptador {
        fun actualizarAdaptador(listaPlantas: ListaPlantas)
    }
}