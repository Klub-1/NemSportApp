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
        user = intent.extras!!.get("user") as UserModel
        uid = intent.extras!!.getString("uid", Firebase.auth.currentUser?.uid ?: getString(R.string.unknown))
        mail = intent.extras!!.get("mail") as String

        editProfilePhoneET = findViewById(R.id.editProfilePhoneEt)
        editProfileMailET = findViewById(R.id.editProfileMailEt)

        val editProfileNameTxt: TextView = findViewById(R.id.editProfileNameTxt)

        val editProfileAcceptBtn: Button = findViewById(R.id.editProfileAcceptBtn)
        val editProfileCancelBtn: Button = findViewById(R.id.editProfileCancelBtn)

        if (user != null && mail != null) {
            editProfileNameTxt.text = user.username
            editProfilePhoneET.setText(user.phonenumber)
            editProfileMailET.setText(mail)
        }



        editProfileAcceptBtn.setOnClickListener{
            showdialog()
        }

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
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.confirm_updates))

        val input = EditText(this)

        input.setHint(getString(R.string.input_password_to_confirm))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            input.setAutofillHints(View.AUTOFILL_HINT_PASSWORD)
        }

        input.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        builder.setView(input)

        builder.setPositiveButton(getString(R.string.edit_userpage_accept), DialogInterface.OnClickListener { dialog, which ->
            var succes: Boolean = true
            passwordUser = input.text.toString()
            if (uid != getString(R.string.unknown) && passwordUser.isNotEmpty()){


                    Firebase.auth.currentUser!!.reauthenticate(EmailAuthProvider
                        .getCredential(mail, passwordUser))

                        .addOnCompleteListener{
                            if (editProfileMailET.text.toString().isNotEmpty() && mail != editProfileMailET.text.toString()) {
                                Firebase.auth.currentUser?.updateEmail(editProfileMailET.text.toString())!!
                                    .addOnFailureListener {
                                        succes = false
                                        Toast.makeText(this, getString(R.string.fail_update_email), Toast.LENGTH_LONG).show()
                                }
                            }

                            if (editProfilePhoneET.text.toString().isNotEmpty() && user.phonenumber != editProfilePhoneET.text.toString()) {
                                user.phonenumber = editProfilePhoneET.text.toString()
                                DatabaseHelper.updateUserInDB(uid, user)
                                    .addOnSuccessListener { succes = true }
                                    .addOnFailureListener {
                                            e-> Log.w("UPDATE_USER", e.toString())
                                        succes = false
                                        Toast.makeText(this, getString(R.string.fail_update_phonenumber), Toast.LENGTH_LONG).show()
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

        builder.setNegativeButton(getString(R.string.edit_userpage_cancel), DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }

}