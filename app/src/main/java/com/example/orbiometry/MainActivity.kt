package com.example.orbiometry

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import model.entities.User
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    companion object {
        val users = mutableListOf<User>()
        fun searchUser(username: String): Int? {
            var index: Int = 0
            users.forEach {
                if (it.username == username)
                    return index
                index++
            }
            return null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        users.add(User("admin", "admin"))
        users.add(User("guest", "guest"))

        executor = ContextCompat.getMainExecutor(this@MainActivity)
        biometricPrompt = BiometricPrompt(this@MainActivity, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                goHomeActivity()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Biometric login for ORBiometry App").setSubtitle("Log in using your biometric credential").setNegativeButtonText("Cancel").build()


        val btnFp = findViewById<ImageView>(R.id.btn_fingerprint)
        btnFp.setOnClickListener {
            val username = findViewById<TextView>(R.id.username_login)
            val indexUser: Int? = searchUser(username.text.toString())
            if (indexUser != null) {
                if (users[indexUser].logged && User.usedFingerprint && users[indexUser].fingerPrintPermission && users[indexUser].username == username.text.toString()) {
                    biometricPrompt.authenticate(promptInfo)
                }
            }
        }
    }

    fun signUp(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    fun authentication(view: View) {
        val username = findViewById<TextView>(R.id.username_login)
        val password = findViewById<TextView>(R.id.password_login)
        if (username.text.toString().equals("") || password.text.toString().equals("")) {
            notify("Username or password is empty! ")
        } else {
            val indexUser: Int? = searchUser(username.text.toString())
            if (indexUser != null) {
                val user: User = users.get(indexUser)
                if (User.authenticate(user, username.text.toString(), password.text.toString())) {
                    if (!user.logged && !User.usedFingerprint) {
                        fingerprintAccess(indexUser)
                    } else if (!user.logged && User.usedFingerprint) {
                        users[indexUser].logged = true
                        goHomeActivity()
                    } else {
                        goHomeActivity()
                    }
                } else {
                    notify("Incorrect username or password!")
                }
            } else {
                notify("This user does not exist")
            }
        }
    }

    private fun fingerprintAccess(indexUser: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Biometric Access Request!").setMessage("Would you like to use biometric access the next time you log in?")
                .setPositiveButton("Yes"
                ) { dialog, id ->
                    users[indexUser].logged = true
                    users[indexUser].fingerPrintPermission = true
                    User.usedFingerprint = true
                    goHomeActivity()
                }
                .setNegativeButton("No"
                ) { dialog, id ->
                    users[indexUser].logged = true
                    goHomeActivity()
                }
        builder.create()
        builder.show()
    }

    fun goHomeActivity() {
        notify("Login successful")
        val homePage = Intent(this, HomeActivity::class.java)
        startActivity(homePage)
        finish()
    }

    private fun notify(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}