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

        loginBtn.setOnClickListener{
            val mail: String = mailEt.text.toString()
            val psw: String = pswEt.text.toString()
            if (mail != null && psw != null){ login(mail = mail, psw = psw) }
        }

        to_signupBtn.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }

    private fun login(mail: String, psw: String) {
        auth.signInWithEmailAndPassword(mail, psw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "signInWithEmail:success")
                    startActivity(Intent(this, FragmentContainerActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                    // Based on
                    // https://stackoverflow.com/questions/40093781/check-if-given-email-exists/40095155
                    auth.fetchSignInMethodsForEmail(mail)
                        .addOnCompleteListener(OnCompleteListener<SignInMethodQueryResult> { task ->
                            val isNewUser = task.result!!.signInMethods!!.isEmpty()
                            if (isNewUser) {
                                Log.e("LOGIN", "Is New User!")
                            } else {
                                Log.e("LOGIN", "Is Old User!")
                            }
                        })
                }
            }
    }
}