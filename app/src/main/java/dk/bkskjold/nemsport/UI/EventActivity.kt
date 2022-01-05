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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        var event:EventModel = intent.extras?.get("event") as EventModel

        val eventName = findViewById<TextView>(R.id.eventNameTextView)
        val eventDesc = findViewById<TextView>(R.id.desc)
        val backBtn = findViewById<Button>(R.id.backBtn)
        val authorTextView = findViewById<TextView>(R.id.authTextView)
        val eventDestination = findViewById<TextView>(R.id.placeTextView)
        val participants: RecyclerView = findViewById(R.id.userRecyclerView)

        eventDesc.text = event.eventDescription
        eventName.text = event.eventName
        eventDestination.text = event.pitches

        lifecycleScope.launch {
            val UID = event.eventCreaterUID.toString()
            authorTextView.text = DatabaseHelper.getUserFromDB(UID)?.username.toString()
        }

        Log.d("DBHelper", "DocumentSnapshot added with ID:")

        var participantsItemArray = ArrayList<StringItem>()

        for (i in 0..20){
            participantsItemArray.add(i,StringItem((i.toString())))
        }

        //adds newly created list of StringItems
        participants.adapter = ParticipantsEventAdapter(participantsItemArray)



        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }
    }
}