package dk.bkskjold.nemsport.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dk.bkskjold.nemsport.R

class CreateEventActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val createEvent: Button = findViewById(R.id.createBtn)
        val backBtn: Button = findViewById(R.id.backBtn)
        createEvent.setOnClickListener{}

        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }

    }

    }
