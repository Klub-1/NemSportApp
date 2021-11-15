package dk.bkskjold.nemsport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dk.bkskjold.nemsport.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signInBtn: Button = findViewById(R.id.loginPageBtn)
        val signUpGuestBtn: Button = findViewById(R.id.signUpGuestBtn)
        val signUpMemberBtn: Button = findViewById(R.id.signUpMemberBtn)

        signUpGuestBtn.setOnClickListener{
            startActivity(Intent(this, FragmentContainerActivity::class.java))
        }
        signUpMemberBtn.setOnClickListener{
            startActivity(Intent(this, FragmentContainerActivity::class.java))
        }
        signInBtn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}