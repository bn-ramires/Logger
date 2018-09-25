package com.example.oresama.cyclelogger

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.InetAddress

class SignUpActivity : AppCompatActivity() {

    private lateinit var fireBaseAuth: FirebaseAuth
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        fireBaseAuth = FirebaseAuth.getInstance()

        sign_up_btn.setOnClickListener {

            doAsync {

                if (isInternetAvailable()) {

                    uiThread {
                        progressDialog = ProgressDialog(it)
                        progressDialog?.setMessage(getString(R.string.loading_message))
                        progressDialog?.show()

                        val email = email_edit_text.text.toString()
                        val password = pw_edit_text.text.toString()

                        fireBaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {

                                        progressDialog?.dismiss()
                                        startActivity(intentFor<UserPanelActivity>(
                                                "email" to email,
                                                "password" to password))

                                        toast(getString(R.string.new_user_registered))

                                    } else {
                                        progressDialog?.dismiss()
                                        toast(getString(R.string.operation_failed))
                                    }
                                }
                    }
                } else {
                    uiThread { toast(R.string.no_internet) }
                }
            }
        }

        already_have_account.setOnClickListener { this.finish() }
    }

    private fun isInternetAvailable(): Boolean {
        try {
            val address = InetAddress.getByName("www.google.com")
            return !address.equals("")
        } catch (e: Exception) {
            return false
        }
    }
}

