package com.example.orbiometry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import model.entities.User

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    public fun signup(view: View){
        val username = findViewById<TextView>(R.id.username_register)
        val password = findViewById<TextView>(R.id.password_register)
        MainActivity.users.add(User(username.text.toString(), password.text.toString()))
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}