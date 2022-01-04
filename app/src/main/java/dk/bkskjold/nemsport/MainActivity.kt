package dk.bkskjold.nemsport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth





class MainActivity : AppCompatActivity() {

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // Choose authentication providers
    private val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
    // Create and launch sign-in intent
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setTheme(R.style.LoginTheme)
        .setLogo(R.mipmap.logo_bkskjold)
        .setAvailableProviders(providers)
        .build()

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
            signInLauncher.launch(signInIntent)
        }


    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
            val response = result.idpResponse
            if (result.resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                startActivity(Intent(this, FragmentContainerActivity::class.java))
                // ...
            } else {
                signInLauncher.launch(signInIntent)
            }
        }

}