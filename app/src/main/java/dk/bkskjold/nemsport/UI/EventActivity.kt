package dk.bkskjold.nemsport.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import org.w3c.dom.Text


class EventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        var test:EventModel = intent.extras?.get("event") as EventModel
        val evetName = findViewById<TextView>(R.id.eventNameTextView)
        val evetDes = findViewById<TextView>(R.id.desc)
        val backBtn = findViewById<Button>(R.id.backBtn)
        evetDes.text = test.eventDescription
        evetName.text = test.eventName
        Log.d("DBHelper", "DocumentSnapshot added with ID:")




        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }
    }
}