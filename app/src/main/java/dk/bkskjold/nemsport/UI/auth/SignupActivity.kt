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
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.UserModel
import dk.bkskjold.nemsport.R

class SignupActivity : AppCompatActivity() {
    // Based on
    // https://firebase.google.com/docs/auth/android/password-auth

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameEt: EditText = findViewById(R.id.usernameEt)
        val phoneEt: EditText = findViewById(R.id.phoneEt)
        val mailEt: EditText = findViewById(R.id.mailEt)
        val pswEt: EditText = findViewById(R.id.pswEt)
        val signupBtn: Button = findViewById(R.id.signupBtn)
        val to_loginBtn: Button = findViewById(R.id.to_loginBtn)

        auth = Firebase.auth

        // Calls signup method on click
        signupBtn.setOnClickListener{
            val username: String = usernameEt.text.toString()
            val phonenumber: String = phoneEt.text.toString()
            val mail: String = mailEt.text.toString()
            val psw: String = pswEt.text.toString()
            if (checkforempty(username) && checkforempty(phonenumber) && checkforempty(mail) && checkforempty(psw)){
                if (psw.length > 5) {
                    signup(username, phonenumber, mail, psw)
                } else {
                    Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
                }
            } // If no field is empty, calls sign up method with entered information
        }

        // Sends user back to login screen
        to_loginBtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
    // checks if string is empty
    private fun checkforempty(txt: String) : Boolean {
        // Triple check to make sure string is empty
        if (txt == null){ return false }
        if (txt.isEmpty()){ return false }
        if (txt.isBlank()){ return false }
        return true
    }

    private fun signup(username: String, phonenumber: String, mail: String, psw: String) {
        // Firebase command to create user using email and passwrod
        auth.createUserWithEmailAndPassword(mail, psw)
            .addOnCompleteListener(this) { task ->
                // check if sign up is succesful
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNUP", "createUserWithEmail:success - " + (auth.currentUser?.uid ?: "FEJL"))
                    // Get user ID
                    val uid: String? = auth.currentUser?.uid
                    // If user ID is not null upload phone number and name in database
                    if (uid != null){
                        DatabaseHelper.createUserInDB(uid,UserModel(username, phonenumber))
                    }
                    startActivity(Intent(this, FragmentContainerActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}