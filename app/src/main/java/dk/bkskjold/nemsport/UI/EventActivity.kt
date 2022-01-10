package dk.bkskjold.nemsport.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Adapter.ParticipantsEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.StringItem
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch


class EventActivity : AppCompatActivity() {
    var participantsItemArray = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_event)
        var event: EventModel = intent.extras?.get("event") as EventModel

        val eventName = findViewById<TextView>(R.id.eventNameTextView)
        val eventDesc = findViewById<TextView>(R.id.desc)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val authorTextView = findViewById<TextView>(R.id.authTextView)
        val eventDestination = findViewById<TextView>(R.id.placeTextView)
        val participants: RecyclerView = findViewById(R.id.userRecyclerView)

        eventDesc.text = event.eventDescription
        eventName.text = event.eventName
        eventDestination.text = event.pitches

        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        if (!event.participants.contains(event.eventCreaterUID)) {
            signUpBtn.text = "afmeld"
            true
        } else {
            signUpBtn.text = "tilmeld"
            true
        }


        lifecycleScope.launch {
            val UID = event.eventCreaterUID.toString()
            authorTextView.text = DatabaseHelper.getUserFromDB(UID)?.username.toString()
        }

        Log.d("DBHelper", "DocumentSnapshot added with ID:")

        var participantsItemArray = ArrayList<String>()


        //adds newly created list of StringItems


        signUpBtn.setOnClickListener {
            if (!event.participants.contains(event.eventCreaterUID)) {
                lifecycleScope.launch {
                    event.participants.add(event.eventCreaterUID)
                    DatabaseHelper.updateParticapents(event)
                    signUpBtn.text = "afmeld"
                    true
                    createRC(event,participants)
                }
            } else {
                lifecycleScope.launch {
                    event.participants.remove(event.eventCreaterUID)
                    DatabaseHelper.updateParticapents(event)
                    signUpBtn.text = "tilmeld"
                    true
                    createRC(event,participants)
                }
            }
        }


        lifecycleScope.launch {
            for (people in event.participants) {
                var person = DatabaseHelper.getUserFromDB(people)
                person?.let { participantsItemArray.add(it.username) }
                participants.adapter = ParticipantsEventAdapter(participantsItemArray)
            }

            backBtn.setOnClickListener {
                // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
                super.onBackPressed()
            }
        }

    }
    fun createRC(event: EventModel, participants: RecyclerView) {
        participantsItemArray.clear()
        lifecycleScope.launch {
            for (people in event.participants) {
                var person = DatabaseHelper.getUserFromDB(people)
                person?.let { participantsItemArray.add(it.username) }
            }
            participants.adapter = ParticipantsEventAdapter(participantsItemArray)

        }
    }

}