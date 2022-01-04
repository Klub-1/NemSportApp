package dk.bkskjold.nemsport.UI.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.R

class SignupActivity : AppCompatActivity() {
    // Based on
    // https://firebase.google.com/docs/auth/android/password-auth

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameEt: EditText = findViewById(R.id.usernameEt)
        val mailEt: EditText = findViewById(R.id.mailEt)
        val pswEt: EditText = findViewById(R.id.pswEt)

        val signupBtn: Button = findViewById(R.id.signupBtn)
        val to_loginBtn: Button = findViewById(R.id.to_loginBtn)

        auth = Firebase.auth

        signupBtn.setOnClickListener{
            val usrname: String = usernameEt.text.toString()
            val mail: String = mailEt.text.toString()
            val psw: String = pswEt.text.toString()
            if (usernameEt != null && mail != null && psw != null){ signup(mail = mail, psw = psw) }
        }

        to_loginBtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun signup(mail: String, psw: String) {
        auth.createUserWithEmailAndPassword(mail, psw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNUP", "createUserWithEmail:success - " + (auth.currentUser?.uid ?: "FEJL"))

                    db.collection("cities").document("LA")
                        .set(city)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                    startActivity(Intent(this, FragmentContainerActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}