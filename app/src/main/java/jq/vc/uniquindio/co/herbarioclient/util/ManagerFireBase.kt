package jq.vc.uniquindio.co.herbarioclient.util

import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas

class ManagerFireBase private constructor() {
    private var database: FirebaseDatabase? = null

    private var dataRef: DatabaseReference? = null
    //hace referencia a la bd

    init {
        //para inicializar antes de que el constructor secundario se inicialice

        database = FirebaseDatabase.getInstance()
        dataRef = database!!.reference
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

    fun insetarConLLave(listaPlantas: ListaPlantas) {
        //
        dataRef!!.child("1").setValue(listaPlantas)
    }

    fun eliminar() {
    }

    fun actualizar() {
    }
}