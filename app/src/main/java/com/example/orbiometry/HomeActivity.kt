package com.example.orbiometry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    public fun logout(view : View){
        val login_page = Intent(this, MainActivity::class.java)
        startActivity(login_page)
        finish()
    }
}