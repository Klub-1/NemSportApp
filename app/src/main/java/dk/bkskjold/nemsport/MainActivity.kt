package dk.bkskjold.nemsport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import dk.bkskjold.nemsport.UI.auth.LoginActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        // Check memory if a user is already logged in
        // Inspired by user @Leenah on Stackoverflow:
        // https://stackoverflow.com/questions/22262463/firebase-how-to-keep-an-android-user-logged-in
        val user = null // FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            startActivity(Intent(this, FragmentContainerActivity::class.java))
        } else {
            // User is signed out, start login process
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }

}