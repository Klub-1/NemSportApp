package dk.bkskjold.nemsport.UI.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.R
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener

class LoginActivity : AppCompatActivity() {

    // Based on
    // https://firebase.google.com/docs/auth/android/password-auth

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mailEt: EditText = findViewById(R.id.mailEt)
        val pswEt: EditText = findViewById(R.id.pswEt)
        val loginBtn: Button = findViewById(R.id.loginBtn)
        val to_signupBtn: Button = findViewById(R.id.to_signupBtn)
        val forgotPswTxt: TextView = findViewById(R.id.forgotPswTxt)
        auth = Firebase.auth

        // Sends user to forgot password screen
        forgotPswTxt.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Logs user in with entered email and password
        loginBtn.setOnClickListener{
            val mail: String = mailEt.text.toString().trim()
            val psw: String = pswEt.text.toString()
            if (checkforempty(mail) && checkforempty(psw)){ login(mail = mail, psw = psw) } // If fields aren't empty attempts to login with entered credentials
        }

        // Sends user to sign up screen
        to_signupBtn.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    // Checks for empty string
    private fun checkforempty(txt: String) : Boolean {
        // Triple checks to make sure if string is empty
        if (txt == null){ return false }
        if (txt.isEmpty()){ return false }
        if (txt.isBlank()){ return false }
        return true
    }

    // Login method
    private fun login(mail: String, psw: String) {
        auth.signInWithEmailAndPassword(mail, psw) // Firestore commmand for login with entered mail and password
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "signInWithEmail:success")
                    startActivity(Intent(this, FragmentContainerActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                }
            }
    }
}