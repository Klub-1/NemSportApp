package dk.bkskjold.nemsport.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Adapter.TodayEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.firebase.Timestamp
import dk.bkskjold.nemsport.R
import java.util.*


import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var eventAdapter: TodayEventAdapter
    private var _binding: FragmentHomeBinding? = null
    private var _eventList : MutableList<EventModel> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val todayRecyclerView: RecyclerView = binding.todayRV
        eventAdapter = TodayEventAdapter(_eventList)
        todayRecyclerView.adapter = eventAdapter
        todayRecyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)

        lifecycleScope.launch {
            _eventList += cleanEventDataInput(DatabaseHelper.getEventsFromDB())
            eventAdapter.notifyDataSetChanged()
        }

        return root
    }

    /**
     *
     */
    private fun cleanEventDataInput(eventList: MutableList<EventModel>) : MutableList<EventModel> {

        var newEventList: MutableList<EventModel> = mutableListOf()
        val todayString = getString(R.string.home_today_text)
        val upcomingString = getString(R.string.home_upcoming_text)
        val emptyString = getString(R.string.home_empty_text)
        var todayDivider = false
        var upcomingDivider = false


        eventList.sortBy { it.eventTime }

        // The following lines are only required because java/kotlin
        // Has no buildt in way of extracting only the time from a date/timestamp object
        val todayDate = Timestamp.now().toDate()
        val calOnlyToday = Calendar.getInstance()
        calOnlyToday.time = todayDate
        calOnlyToday[Calendar.HOUR_OF_DAY] = 0
        calOnlyToday[Calendar.MINUTE] = 0
        calOnlyToday[Calendar.SECOND] = 0
        calOnlyToday[Calendar.MILLISECOND] = 0
        val todayDateNoTime = calOnlyToday.time
        calOnlyToday.add(Calendar.DATE, 1)
        val tomorrowDateNoTime = calOnlyToday.time

        Log.w("DEBUGME", tomorrowDateNoTime.toString())

        for (event in eventList) {
            if (event.eventTime.toDate() >= todayDateNoTime && event.eventTime.toDate() < tomorrowDateNoTime ) {
                if (!todayDivider) {
                    newEventList.add(EventModel(eventName = todayString, pitches = "TopSecret"))
                    todayDivider = true
                }
                newEventList.add(event)


            } else if (event.eventTime.toDate()  > tomorrowDateNoTime) {
                if (!upcomingDivider) {
                    newEventList.add(EventModel(eventName = upcomingString, pitches = "TopSecret"))
                    upcomingDivider = true
                }
                newEventList.add(event)
            }


        }

        if (eventList.size == 0) {
            newEventList.add(EventModel(eventName = emptyString, pitches = "TopSecret"))
        }

        return newEventList
    }
}
