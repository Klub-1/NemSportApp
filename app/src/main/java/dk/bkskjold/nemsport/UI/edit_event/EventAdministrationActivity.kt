package dk.bkskjold.nemsport.UI.edit_event

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Adapter.CalendarEventAdapter
import dk.bkskjold.nemsport.Adapter.EditEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.util.*

class EventAdministrationActivity : AppCompatActivity() {

    private var _eventList: MutableList<EventModel> = mutableListOf()
    private lateinit var eventAdapter: EditEventAdapter
    private lateinit var backToCalendarBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_administration)

        val eventRecyclerView: RecyclerView = findViewById(R.id.edit_eventsRV)
        eventAdapter = EditEventAdapter(_eventList)
        eventRecyclerView.adapter = eventAdapter

        backToCalendarBtn = findViewById(R.id.backToCalendarBtn)

        backToCalendarBtn.setOnClickListener{
            super.onBackPressed()
        }

        val uid: String = Firebase.auth.currentUser?.uid ?: getString(R.string.unknown)

        if (uid != getString(R.string.unknown)) {
            lifecycleScope.launch {
                _eventList += DatabaseHelper.getEventsCreatedByUserFromDB(uid)
                eventAdapter.notifyDataSetChanged()
            }
        }

    }
}