package dk.bkskjold.nemsport.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat


class EventActivity : AppCompatActivity() {

    private lateinit var UID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        var event:EventModel = intent.extras?.get("event") as EventModel

        val eventCreator = findViewById<TextView>(R.id.eventCreatorTextView)
        val eventName = findViewById<TextView>(R.id.eventNameTextView)
        val evetDes = findViewById<TextView>(R.id.desc)
        val locationTxt = findViewById<TextView>(R.id.locationTxt)
        val dateTxt = findViewById<TextView>(R.id.dateTxt)
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        val backBtn = findViewById<Button>(R.id.backBtn)


        eventName.text = event.eventName
        evetDes.text = event.eventDescription

        val dateFormat: DateFormat = getDateTimeInstance() //SimpleDateFormat("yyyy-MM-DD HH:MM")

        dateTxt.text = dateFormat.format(event.eventTime.toDate())

        locationTxt.text = event.pitches

        UID = event.eventCreaterUID.toString()


        if (UID == Firebase.auth.currentUser!!.uid) {
            signUpBtn.visibility = View.GONE
            eventCreator.text = getString(R.string.event_owner_desc)
        } else {
            signUpBtn.visibility = View.VISIBLE
            lifecycleScope.launch {
                eventCreator.text = DatabaseHelper.getUserFromDB(UID)?.username.toString()
                Log.d("DBHelper", "DocumentSnapshot added with ID:")
            }
        }


        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }
    }
}