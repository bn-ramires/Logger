package com.example.oresama.cyclelogger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_panel.*

class UserPanelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_panel)

        val email = intent.getStringExtra("email")

        welcome_message.text = "You have logged in as $email"
    }
}
