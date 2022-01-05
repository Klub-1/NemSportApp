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
            _eventList += DatabaseHelper.getEventsFromDB()
            _eventList.sortBy { it.eventTime }
            eventAdapter.notifyDataSetChanged()
        }



        return root
    }




}