package dk.bkskjold.nemsport.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dk.bkskjold.nemsport.Adapter.CalendarEventAdapter
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import java.util.ArrayList

class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    var FAB_IS_OPEN: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarViewSearch: CalendarView = view.findViewById(R.id.calendarViewSearch)

        val menuFab: FloatingActionButton = view.findViewById(R.id.menuFab)
        val createFab: FloatingActionButton = view.findViewById(R.id.createFab)
        val filterFab: FloatingActionButton = view.findViewById(R.id.filterFab)

        menuFab.setOnClickListener {
            if(FAB_IS_OPEN){
                menuFab.setImageResource(R.drawable.ic_action_menu_light)
                createFab.visibility = View.GONE
                filterFab.visibility = View.GONE
                FAB_IS_OPEN = false
            }else{
                menuFab.setImageResource(R.drawable.ic_action_close_light)
                createFab.visibility = View.VISIBLE
                filterFab.visibility = View.VISIBLE
                FAB_IS_OPEN = true
            }
        }

        createFab.setOnClickListener{
            startActivity(Intent(view.context, CreateEventActivity::class.java))
        }

        createCalenderView(view)


        // Inflate the layout for this fragment
        return view
    }

    private fun createCalenderView(view: View){

        val today_event_recyclerview = view.findViewById<RecyclerView>(R.id.calendarRV)

        today_event_recyclerview.layoutManager = LinearLayoutManager(view.context)

        val data = ArrayList<EventModel>()

        data.add(EventModel("8:00 - 10:00", "Husk fodbold", "Fodboldgolf", false))
        data.add(EventModel("12:00 - 13:30", "Afholdes i kantinen", "Bestyrelsesmøde", true))
        data.add(EventModel("14:00 - 15:30", "Lokale 101", "Generélforsamling", false))
        data.add(EventModel("20:00 - 21:30", "Træneren holder fri", "Fodboldtræning", true))

        val adapter = CalendarEventAdapter(data)

        today_event_recyclerview.adapter = adapter
    }


}