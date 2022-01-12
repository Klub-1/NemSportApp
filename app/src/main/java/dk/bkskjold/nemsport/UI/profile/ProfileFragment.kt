package dk.bkskjold.nemsport.UI.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.UserModel
import dk.bkskjold.nemsport.R
import dk.bkskjold.nemsport.UI.EventActivity
import dk.bkskjold.nemsport.UI.auth.LoginActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var user: UserModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val profileNameTxt: TextView = view.findViewById(R.id.profileNameTxt)
        val profilePhoneTxt: TextView = view.findViewById(R.id.profilePhoneTxt)
        val profileMailTxt: TextView = view.findViewById(R.id.profileMailTxt)
        val changeProfileBtn: Button = view.findViewById(R.id.editProfileBtn)
        val logoutBtn: Button = view.findViewById(R.id.logoutBtn)
        val uid: String = Firebase.auth.currentUser?.uid ?: getString(R.string.unknown)

        // Check if user id is found and get user data from firebase
        if (!uid.equals(getString(R.string.unknown))){
            lifecycleScope.launch {
                // Gets information from Firestore and set fields with that info
                user = DatabaseHelper.getUserFromDB(uid)!!
                profileNameTxt.text = user!!.username
                profilePhoneTxt.text = user!!.phonenumber
            }
            profileMailTxt.text = Firebase.auth.currentUser?.email ?: getString(R.string.unknown)
        }

        // Go to edit profile page
        changeProfileBtn.setOnClickListener{
            val intent = Intent(view.context, EditProfileActivity::class.java)
            if (user != null){
                intent.putExtra("user",user)
                intent.putExtra("uid", uid)
                intent.putExtra("mail", profileMailTxt.text.toString())
                startActivity(intent)
            }
        }
        
        // Logout of firebase and go to login page
        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(view.context, LoginActivity::class.java))
        }
        return view
    }
}