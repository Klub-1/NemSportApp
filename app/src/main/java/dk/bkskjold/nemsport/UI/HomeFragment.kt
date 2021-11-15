package dk.bkskjold.nemsport.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Adapter.TodayEventAdapter
import dk.bkskjold.nemsport.Adapter.TomorrowEventAdapter
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import java.util.ArrayList


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        createTodayView(view)
        createTomorrowView(view)

        return view
    }

    private fun createTodayView(view: View){

        val today_event_recyclerview = view.findViewById<RecyclerView>(R.id.todayRV)

        today_event_recyclerview.layoutManager = LinearLayoutManager(view.context)

        val data = ArrayList<EventModel>()

        data.add(EventModel("8:00 - 10:00", "", "Fodboldgolf", false))
        data.add(EventModel("20:00 - 21:30", "", "Fodboldtræning", false))

        val adapter = TodayEventAdapter(data)

        today_event_recyclerview.adapter = adapter
    }

    private fun createTomorrowView(view: View){

        val tomorrow_event_recyclerview = view.findViewById<RecyclerView>(R.id.tomorrowRV)

        tomorrow_event_recyclerview.layoutManager = LinearLayoutManager(view.context)

        val data = ArrayList<EventModel>()

        data.add(EventModel("12:00 - 14:00", "", "Hyggebold", false))
        data.add(EventModel("20:00 - 24:00", "", "Afslutningsfest", false))

        val adapter = TomorrowEventAdapter(data)

        tomorrow_event_recyclerview.adapter = adapter
    }

}