package dk.bkskjold.nemsport.UI.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.UserModel
import dk.bkskjold.nemsport.R
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.launch


class EditProfileActivity : AppCompatActivity() {

    private lateinit var passwordUser: String
    private lateinit var uid: String
    private lateinit var user: UserModel
    private lateinit var mail: String
    private lateinit var editProfilePhoneET: EditText
    private lateinit var editProfileMailET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Get user data from intent (ProfileFragment.kt)
        user = intent.extras!!.get("user") as UserModel
        uid = intent.extras!!.getString("uid", DatabaseHelper.getUserUIDFromDB() ?: getString(R.string.unknown))
        mail = intent.extras!!.get("mail") as String

        editProfilePhoneET = findViewById(R.id.editProfilePhoneEt)
        editProfileMailET = findViewById(R.id.editProfileMailEt)
        val editProfileNameTxt: TextView = findViewById(R.id.editProfileNameTxt)
        val editProfileAcceptBtn: Button = findViewById(R.id.editProfileAcceptBtn)
        val editProfileCancelBtn: Button = findViewById(R.id.editProfileCancelBtn)

        // check if user and email is null - if not insert data from user into edittext
        if (user != null && mail != null) {
            editProfileNameTxt.text = user.username
            editProfilePhoneET.setText(user.phonenumber)
            editProfileMailET.setText(mail)
        }

        // Save user data
        editProfileAcceptBtn.setOnClickListener{
            showdialog()
        }

        // Go back to last used activity
        editProfileCancelBtn.setOnClickListener{
            super.onBackPressed()
        }
    }

    /*
    * @BASED ON: https://handyopinion.com/show-alert-dialog-with-an-input-field-edittext-in-android-kotlin/
    * @BASED ON: https://stackoverflow.com/questions/49357150/how-to-update-email-from-firebase-in-android
    *
    */
    fun showdialog(){
        // Init dialog
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        // Set dialog title
        builder.setTitle(getString(R.string.confirm_updates))
        // Create edit text
        val input = EditText(this)
        // Set hint text for edittext
        input.setHint(getString(R.string.input_password_to_confirm))

        // If SDK is high enough set autofillhints to password
        // Used by password manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            input.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
        }

        // Set input type to visible password
        input.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        // Insert edittext into dialog
        builder.setView(input)
        // Create button in dialog (Button is showned on the right side of the view)
        builder.setPositiveButton(getString(R.string.edit_userpage_accept), DialogInterface.OnClickListener { dialog, which ->
            // Succes variable will change to false if something went wrong
            var succes: Boolean = true
            // get text from edittext
            passwordUser = input.text.toString()
            // Check if user ID is found and if edittext is empty
            if (uid != getString(R.string.unknown) && passwordUser.isNotEmpty()){
                    // reauthenticate user
                    Firebase.auth.currentUser!!.reauthenticate(EmailAuthProvider
                        .getCredential(mail, passwordUser))
                        // Reauthenticate complete
                        .addOnCompleteListener{
                            // check if mail is not empty and mail = user currently mail
                            if (editProfileMailET.text.toString().isNotEmpty() && mail != editProfileMailET.text.toString()) {
                                // Update user mail - on fail set succes = false
                                Firebase.auth.currentUser?.updateEmail(editProfileMailET.text.toString())!!
                                    .addOnFailureListener {
                                        succes = false
                                        Toast.makeText(this, getString(R.string.fail_update_email), Toast.LENGTH_LONG).show()
                                }
                            }
                            // check if phone is not empty and phone = user currently phone
                            if (editProfilePhoneET.text.toString().isNotEmpty() && user.phonenumber != editProfilePhoneET.text.toString()) {
                                // Get text from phone edittext
                                user.phonenumber = editProfilePhoneET.text.toString()
                                // Call updateUserInDB method from DatabaseHelper Singleton
                                DatabaseHelper.updateUserInDB(uid, user)
                                    .addOnSuccessListener { succes = true }
                                    .addOnFailureListener {
                                            e-> 
                                        succes = false
                                        Toast.makeText(this, getString(R.string.fail_update_phonenumber), Toast.LENGTH_LONG).show() // Shows message to user on failure
                                    }
                            }
                            if (succes){
                                startActivity(Intent(this, FragmentContainerActivity::class.java))
                            }
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_LONG).show()
                    }
            }
        })
        // Create cancel button in dialog
        builder.setNegativeButton(getString(R.string.edit_userpage_cancel), DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }
}