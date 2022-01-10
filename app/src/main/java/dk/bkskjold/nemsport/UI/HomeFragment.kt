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


    private fun cleanEventDataInput(eventList: MutableList<EventModel>) : MutableList<EventModel> {
        var newEventList: MutableList<EventModel> = mutableListOf()
        val todayTimeStamp = Timestamp.now().toDate()
        val date = Date(todayTimeStamp.time)


        val date = todayTimeStamp.toDate()



        eventList.sortBy { it.eventTime }

        var todayDivider = false
        var upcomingDivider = false

        val todayString = getString(R.string.home_today_text)
        val upcomingString = getString(R.string.home_upcoming_text)
        val emptyString = getString(R.string.home_empty_text)


        for (event in eventList) {
            Log.w("DEBUGME", event.eventTime.toDate().toString() + " Compared to " + todayTimeStamp.toString() + " Whereas: "+ date.toString())
            if (event.eventTime.toDate() == todayTimeStamp ) {
                if (!todayDivider) {
                    newEventList.add(EventModel(eventName = todayString, pitches = "TopSecret"))
                    todayDivider = true
                }
                newEventList.add(event)


            } else if (event.eventTime.toDate()  > todayTimeStamp) {
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
