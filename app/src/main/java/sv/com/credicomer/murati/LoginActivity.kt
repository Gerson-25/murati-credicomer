package sv.com.credicomer.murati

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.analytics.FirebaseAnalytics
import sv.com.credicomer.murati.constants.*
import sv.com.credicomer.murati.databinding.ActivityLoginBinding
import sv.com.credicomer.murati.dialogs.LoginDialogFragment


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Declaration of FirebaseAuthLocalClass components
    private var dbAuth: FirebaseAuth? = null
    // End of declaration FirebaseAuthLocalClass components

    // Crashlytics
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    // Activity Lifecycle methods here

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)



        // Validar si el usuario ha logeado anteriormente

        dbAuth = FirebaseAuth.getInstance()
        val user = dbAuth!!.currentUser

        // Si usuario NO es null entonces LoginActivity NO se ejecuta y pasa directamente a MainActivity
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonSignIn.setOnClickListener {
            validateUserSession()
        }
        binding.txtSigninProblem.setOnClickListener {
            showDialog()
        }
    }

    private fun validateUserSession(){

        // Validar los campos Email & Password
        if (binding.etEmail.text.toString().isEmpty() or binding.etPassword.text.toString().isEmpty()) {
            Toast.makeText(this, R.string.required_email_password, Toast.LENGTH_SHORT)
                .show()
        } else {
            dbAuth?.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())?.addOnCompleteListener {
                    if (it.isSuccessful) {

                        // Si authentication funciona aqui se maneja




                        val userLoggedIn = dbAuth?.currentUser

                        // Guardar los datos en SharedPreferences (Informacion Local)

                        val sharedPreferences = this.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
                        editor.putString(FIREBASE_USER_EMAIL_LOGGED_IN_KEY, userLoggedIn!!.email)
                        editor.putString(FIREBASE_USER_UID_KEY, userLoggedIn.uid)
                        editor.apply()

                        var userUID = sharedPreferences.getString(FIREBASE_USER_UID_KEY, "")




                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {

                        // Si authentication falla aqui puede manejarse

                        Toast.makeText(this,R.string.wrong_email_password, Toast.LENGTH_LONG).show()
                    }
                }
        }

    }
    fun showDialog(){
        val fm = this.supportFragmentManager
        val dialog = LoginDialogFragment.newInstance()
        dialog.show(fm, LOGIN_DIALOG)

    }

}
