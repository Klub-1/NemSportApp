package dk.bkskjold.nemsport.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import dk.bkskjold.nemsport.Adapter.TodayEventAdapter
import dk.bkskjold.nemsport.Adapter.TomorrowEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper


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

        val data = ArrayList<EventModel>()

        val todayRecyclerView: RecyclerView = binding.todayRV
        eventAdapter = TodayEventAdapter(_eventList)
        todayRecyclerView.adapter = eventAdapter
        todayRecyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)

        val adapter = TodayEventAdapter(data)

        lifecycleScope.launch {
            _eventList += DatabaseHelper.getEventsFromDB()
            eventAdapter.notifyDataSetChanged()
        }

    private fun createTomorrowView(view: View){

        val tomorrow_event_recyclerview = view.findViewById<RecyclerView>(R.id.tomorrowRV)

        tomorrow_event_recyclerview.layoutManager = LinearLayoutManager(view.context)

        return root
    }

        //data.add(EventModel("12:00 - 14:00", "", "Hyggebold", false))
        //data.add(EventModel("20:00 - 24:00", "", "Afslutningsfest", false))

        val adapter = TomorrowEventAdapter(data)

        tomorrow_event_recyclerview.adapter = adapter
    }



}