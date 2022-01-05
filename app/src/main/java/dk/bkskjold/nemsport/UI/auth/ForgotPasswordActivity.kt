package dk.bkskjold.nemsport.UI.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val mailEt: EditText = findViewById(R.id.mailEt)

        val forgotPasswordBtn: Button = findViewById(R.id.forgotPasswordBtn)

        val auth = Firebase.auth

        forgotPasswordBtn.setOnClickListener {
            val mail: String = mailEt.text.toString()

            if (checkforempty(mail)){
                // Based on
                    // https://morioh.com/p/e96e178d3510
                auth.sendPasswordResetEmail(mail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, LoginActivity::class.java))
                        } else {
                            Toast.makeText(this, "Unable to send password reset", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

    }

    private fun checkforempty(txt: String) : Boolean {

        if (txt == null){ return false }
        if (txt.isEmpty()){ return false }
        if (txt.isBlank()){ return false }

        return true
    }

}