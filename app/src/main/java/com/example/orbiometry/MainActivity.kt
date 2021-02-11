package com.example.orbiometry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this@MainActivity)
        biometricPrompt = BiometricPrompt(this@MainActivity, executor, object : BiometricPrompt.AuthenticationCallback(){

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val control_page_admin = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(control_page_admin)
                finish()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Fingerprint method").setSubtitle("Your security at the finger point").setNegativeButtonText("Use practicality").build()

        val btn_fp = findViewById<ImageView>(R.id.btn_fingerprint)

        btn_fp.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    public fun authentication(view: View){
        val username = findViewById<TextView>(R.id.username_login)
        val password = findViewById<TextView>(R.id.password_login)

        if(username.text.toString().equals("admin") && password.text.toString().equals("admin")){
            notify("Login successful")
            val control_page_admin = Intent(this, HomeActivity::class.java)
            startActivity(control_page_admin)
            finish()
        } else if(username.text.toString().equals("") || password.text.toString().equals("")){
            notify("Username or password is empty! ")
        } else{
            notify("Incorrect username or password!")
        }
    }

    private fun notify(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}