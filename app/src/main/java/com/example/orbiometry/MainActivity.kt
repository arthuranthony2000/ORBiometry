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
import model.entities.User
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    companion object{
        val users = mutableListOf<User>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        users.add(User("admin","admin"))
        users.add(User("guest", "guest"))

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

        promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for ORBiometry App").setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel").build()

        val btn_fp = findViewById<ImageView>(R.id.btn_fingerprint)

        btn_fp.setOnClickListener {
            val username = findViewById<TextView>(R.id.username_login)
            val user: User? = searchUser(username.text.toString())
            if(user != null){
                if(user.logged && User.usedFingerprint && user.fingerPrintPermission && user.username == username.text.toString()){
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        }
    }

    public fun signup(view: View){
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    public fun authentication(view: View){
        val username = findViewById<TextView>(R.id.username_login)
        val password = findViewById<TextView>(R.id.password_login)
        if(username.text.toString().equals("") || password.text.toString().equals("")){
            notify("Username or password is empty! ")
        } else{
            val user: User? = searchUser(username.text.toString())
            if(user != null){
                if(User.authenticate(user, username.text.toString(), password.text.toString())){
                    if(!user.logged && !User.usedFingerprint){
                        user.logged = true
                        user.fingerPrintPermission = true
                        User.usedFingerprint = true
                    } else if(!user.logged && User.usedFingerprint){
                        user.logged = true
                    }
                    notify("Login successful")
                    val control_page_admin = Intent(this, HomeActivity::class.java)
                    startActivity(control_page_admin)
                    finish()
                } else{
                    notify("Incorrect username or password!")
                }
            } else{
                notify("This user does not exist")
            }
        }

    }

    private fun searchUser(username: String) : User? {
        users.forEach {
            if(it.username == username)
                return it
        }
        return null
    }

    private fun notify(message : String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}