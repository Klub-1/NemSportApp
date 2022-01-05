package dk.bkskjold.nemsport.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch


class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        var event:EventModel = intent.extras?.get("event") as EventModel
        val evetName = findViewById<TextView>(R.id.eventNameTextView)
        val evetDes = findViewById<TextView>(R.id.desc)
        val backBtn = findViewById<Button>(R.id.backBtn)
        evetDes.text = event.eventDescription
        lifecycleScope.launch {
            val UID = event.eventCreaterUID.toString()
            evetName.text = DatabaseHelper.getUserFromDB(UID)?.username.toString()
        }
        Log.d("DBHelper", "DocumentSnapshot added with ID:")




        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }
    }
}