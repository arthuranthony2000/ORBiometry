package model.entities

import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executor

class User(var username: String, var password: String){
    var logged: Boolean = false
    var fingerPrintPermission: Boolean = false
    
    companion object{
        var usedFingerprint: Boolean = false

        fun authenticate(user: User, username: String, password: String): Boolean{
            if(user.username == username && user.password == password){
                return true
            }
            return false
        }

    }
}