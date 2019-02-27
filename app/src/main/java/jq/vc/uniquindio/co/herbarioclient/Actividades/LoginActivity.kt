package jq.vc.uniquindio.co.herbarioclient.Actividades

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.Manifest.permission.READ_CONTACTS
import android.app.ProgressDialog
import android.content.Intent
import android.support.annotation.NonNull
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import jq.vc.uniquindio.co.herbarioclient.R
import jq.vc.uniquindio.co.herbarioclient.util.ManagerFireBase
import jq.vc.uniquindio.co.herbarioclient.vo.ListaPlantas
import jq.vc.uniquindio.co.herbarioclient.vo.Sesion
import jq.vc.uniquindio.co.herbarioclient.vo.Usuarios

import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), LoaderCallbacks<Cursor>, ManagerFireBase.onActualizarAdaptador {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuthTask: UserLoginTask? = null
    lateinit var managerFireBase: ManagerFireBase
    var sesion: Sesion? = null
    var listaUsuarios: ArrayList<Usuarios> = ArrayList()
    var mAuth: FirebaseAuth? = null
    var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = ProgressDialog(this)
        sesion = Sesion(this)

        managerFireBase = ManagerFireBase.managerInstance
        managerFireBase.listener = this
        managerFireBase.escucharEventoFireBase(2,this)


        // Set up the login form.
        populateAutoComplete()
        usuario.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }

        registro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            intent.putExtra("Iniciar Sesion", "1")
            startActivity(intent)
        }


        resetPass.setOnClickListener {
            val emailStr = usuario.text.toString()
            reestablecerPass(emailStr)
        }
    }

    override fun onStart() {
        super.onStart()
//        val user: FirebaseUser = mAuth!!.currentUser!!
    }

    /**
     * Función que permite ingresar mediante email y contraseña a la apliación,
     * esto se valida en firebase de acuerdo al email registrado.
     */
    fun ingresar(email: String, password: String) {
        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with signed-in user's information
                    val user: FirebaseUser = mAuth!!.currentUser!!;
                    Log.d("Log", "signInWithEmail:success" + user.email)
                    sesion!!.setusename(user.email.toString())
                    Log.d("Log", "signInWithEmail:success" + sesion!!.getusename())
                    for (usuarios in listaUsuarios) {
                        Log.d("UsuariosLog","="+usuarios.correo+""+sesion!!.getusename())
                        if (usuarios.correo.equals(sesion!!.getusename())) {
                            sesion!!.setNombre(usuarios.nombre!!)
                            sesion!!.setApellido(usuarios.apellido!!)
                            sesion!!.setUrlFoto(usuarios.urlImagenPerfil!!)
                            sesion!!.setTelefono(usuarios.telefono!!)
                            sesion!!.setProfesion(usuarios.profesion!!)
                            sesion!!.setKey(usuarios.key!!)
                        }
                    }
                    progressDialog!!.dismiss()
                    lanzarActividad()

                } else {
                    progressDialog!!.dismiss()
                    // If sign in fails, display a message to the user.
                    Log.e("", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@LoginActivity, "Ha fallado la autenticación.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }


    /**
     * Función que permite reestablecer contraseña mediante el correo electrónico regsitrado
     */
    fun reestablecerPass(email: String) {

        progressDialog!!.setMessage("Enviando información..")
        progressDialog!!.show()
        if (!TextUtils.isEmpty(email)) {
            mAuth!!
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressDialog!!.dismiss()
                        val message = "Se envió email para reestablecer contraseña a " + email
                        Log.d("Hola", message)
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                    } else {
                        progressDialog!!.dismiss()
                        Log.w("hola", task.exception!!.message)
                        Toast.makeText(this, "Usuario no registrado con este email.", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            progressDialog!!.dismiss()
            var cancel = false
            var focusView: View? = null
            usuario.error = getString(R.string.error_field_required)
            focusView = pass
            cancel = true
        }
    }


    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(usuario, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok,
                    { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }


        // Reset errors.
        usuario.error = null
        pass.error = null

        // Store values at the time of the login attempt.
        val emailStr = usuario.text.toString()
        val passwordStr = pass.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            usuario.error = getString(R.string.error_field_required)
            focusView = pass
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {


            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog!!.setMessage("Validando usuario...")
            progressDialog!!.show()
            ingresar(emailStr, passwordStr)
            // mAuthTask = UserLoginTask(emailStr, passwordStr)
            //mAuthTask!!.execute(null as Void?)


        }
    }

    private fun isEmailValid(email: String): Boolean {
        //TODO: Replace this with your own logic
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO: Replace this with your own logic
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            // Retrieve data rows for the device user's 'profile' contact.
            Uri.withAppendedPath(
                ContactsContract.Profile.CONTENT_URI,
                ContactsContract.Contacts.Data.CONTENT_DIRECTORY
            ), ProfileQuery.PROJECTION,

            // Select only email addresses.
            ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(
                ContactsContract.CommonDataKinds.Email
                    .CONTENT_ITEM_TYPE
            ),

            // Show primary email addresses first. Note that there won't be
            // a primary email address if the user hasn't specified one.
            ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        )
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(
            this@LoginActivity,
            android.R.layout.simple_dropdown_item_1line, emailAddressCollection
        )

        usuario.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY
        )
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class UserLoginTask internal constructor(private val mEmail: String, private val mPassword: String) :
        AsyncTask<Void, Void, Boolean>() {

        val loginActivity = LoginActivity
        override fun doInBackground(vararg params: Void): Boolean? {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                return false
            }

            return DUMMY_CREDENTIALS
                .map { it.split(":") }
                .firstOrNull { it[0] == mEmail }
                ?.let {
                    // Account exists, return true if the password matches.
                    it[1] == mPassword
                }
                ?: true
        }

        override fun onPostExecute(success: Boolean?) {
            mAuthTask = null
            showProgress(false)

            if (success!!) {
                lanzarActividad()
            } else {
                pass.error = getString(R.string.error_incorrect_password)
                pass.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    /**
     * Permite lanzar la actividad
     */
    fun lanzarActividad() {
        val intent = Intent(this, ActivityLogueado::class.java)

        startActivity(intent)
        finish()
    }

    override fun actualizarAdaptador(listaPlantas: ListaPlantas) {

    }

    override fun cedredenciales(usuarios: Usuarios) {
        listaUsuarios(usuarios)
    }

    fun listaUsuarios(usuarios: Usuarios) {
        listaUsuarios.add(usuarios)

    }
}