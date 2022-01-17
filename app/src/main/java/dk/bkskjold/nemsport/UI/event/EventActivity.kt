package dk.bkskjold.nemsport.UI.event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Adapter.ParticipantsEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Helper.DatabaseHelper.getUserUIDFromDB
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class EventActivity : AppCompatActivity() {
    var participantsItemArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event)
        var event: EventModel = intent.extras?.get("event") as EventModel

        val eventName = findViewById<TextView>(R.id.eventNameTextView)
        val eventDesc = findViewById<TextView>(R.id.desc)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val authorTextView = findViewById<TextView>(R.id.eventCreatorTextView)
        val eventDestination = findViewById<TextView>(R.id.locationTxt)
        val participants: RecyclerView = findViewById(R.id.userRecyclerView)
        val dateView = findViewById<TextView>(R.id.dateTxt)
        val sdf = SimpleDateFormat("dd-MM-yyyy - HH:mm")
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        val currentUser = getUserUIDFromDB()!!


        eventDesc.text = event.eventDescription
        eventName.text = event.eventName
        eventDestination.text = event.pitches
        dateView.text = sdf.format(event.eventTime.toDate())

        //sets sign up button text to "afmeld" if user is already signed up for event or "tilmelding" if not
        if (event.participants.contains(currentUser)) {
            signUpBtn.text = "afmeld"
            true
        } else {
            signUpBtn.text = "tilmeld"
            true
        }


        //sets author textview to event creator name
        lifecycleScope.launch {
            val UID = event.eventCreaterUID.toString()
            authorTextView.text = DatabaseHelper.getUserFromDB(UID)?.username.toString()
        }


        var participantsItemArray = ArrayList<String>()



        //adds newly created list of StringItems

        // Sign up
        signUpBtn.setOnClickListener {
            lifecycleScope.launch {
                // Checks if the user is not participating in the event
                if (!event.participants.contains(currentUser) ){
                    lifecycleScope.launch {
                        //Adds current user as participant
                        event.participants.add(currentUser)
                        // updates in database
                        DatabaseHelper.updateParticapents(event)
                        // rewrites button text
                        signUpBtn.text = "afmeld"
                        true
                        createRC(event, participants)
                    }
                } else {
                    // If the user is participating
                    lifecycleScope.launch {
                        // removes current user as participant
                        event.participants.remove(currentUser)
                        // updates in database
                        DatabaseHelper.updateParticapents(event)
                        // rewrites button text
                        signUpBtn.text = "tilmeld"
                        true
                        createRC(event, participants)
                    }
                }
            }
        }


        lifecycleScope.launch {


            //adds all participants to the list
            for (people in event.participants) {
                var person = DatabaseHelper.getUserFromDB(people)
                person?.let { participantsItemArray.add(it.username) }
                participants.adapter = ParticipantsEventAdapter(participantsItemArray)
            }


            backBtn.setOnClickListener {
                // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
                super.onBackPressed()
                finish()
            }
        }



    }
    /*
    creates the recyclerview for participants in the event
    */
    fun createRC(event: EventModel, participants: RecyclerView) {

        participantsItemArray.clear()
        lifecycleScope.launch {
            // getting all participants from database, and adding them to the participantsItemArray
            for (people in event.participants) {
                var person = DatabaseHelper.getUserFromDB(people)
                person?.let { participantsItemArray.add(it.username) }
            }

            // DatabaseHelper.updateParticapents(event)
            
            // defines recyclerviews adapter
            participants.adapter = ParticipantsEventAdapter(participantsItemArray)

        }
    }



}