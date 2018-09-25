package com.example.oresama.cyclelogger

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    private lateinit var fireBaseAuth: FirebaseAuth
    private var progressDialog: ProgressDialog? = null
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fireBaseAuth = FirebaseAuth.getInstance()

        log_in_btn.setOnClickListener {

            doAsync {

                if (isInternetAvailable()) {

                    uiThread {

                        progressDialog = ProgressDialog(this@MainActivity)
                        progressDialog?.setMessage(getString(R.string.loading_message))
                        progressDialog?.show()

                        val email = editTextEmail.text.toString()
                        val password = editTextPassword.text.toString()

                        fireBaseAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {

                                        progressDialog?.dismiss()
                                        startActivity(intentFor<UserPanelActivity>(
                                                "email" to email,
                                                "password" to password))

                                        toast("Logged in as $email")

                                    } else {
                                        progressDialog?.dismiss()

                                        val exception = task.exception
                                        Log.e(tag, "LOGGING EXCEPTION: $exception")

                                        toast(getString(R.string.auth_failed))
                                    }
                                }
                    }
                } else {
                    uiThread { toast(getString(R.string.no_internet)) }
                }
            }
        }

        not_registered_yet.setOnClickListener { startActivity(intentFor<SignUpActivity>()) }
    }
}

private fun isInternetAvailable(): Boolean {
    try {
        val address = InetAddress.getByName("www.google.com")
        return !address.equals("")
    } catch (e: Exception) {
        return false
    }
}