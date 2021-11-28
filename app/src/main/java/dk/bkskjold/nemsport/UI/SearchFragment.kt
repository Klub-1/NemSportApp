package dk.bkskjold.nemsport.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dk.bkskjold.nemsport.Adapter.CalendarEventAdapter
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import java.util.ArrayList

class SearchFragment : Fragment() {

    /*
    SETTINGS VAR AND VAL
     */
    private var FAB_IS_OPEN: Boolean = false


    /*
    VIEWS
     */
    private lateinit var eventRecyclerView: RecyclerView

    private lateinit var menuFab: FloatingActionButton
    private lateinit var createFab: FloatingActionButton
    private lateinit var filterFab: FloatingActionButton

    private lateinit var calendarViewSearch: CalendarView
    private lateinit var calendarToggleCV: CardView

    /*
    RECYCLERVIEW
     */
    private lateinit var adapter: CalendarEventAdapter
    private lateinit var eventList: ArrayList<EventModel>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        initViews(view)
        createCalenderEvent(view)
        fabHandler(view)
        toggleCalenderVisibility(view)


        // Inflate the layout for this fragment
        return view
    }

    private fun toggleCalenderVisibility(view: View) {

    }

    private fun initViews(view: View) {
        calendarViewSearch = view.findViewById(R.id.calendarViewSearch)
        calendarToggleCV = view.findViewById(R.id.calendarToggleCV)

        menuFab = view.findViewById(R.id.menuFab)
        createFab = view.findViewById(R.id.createFab)
        filterFab = view.findViewById(R.id.filterFab)

    }

    private fun fabHandler(view: View) {
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
    }

    private fun createCalenderEvent(view: View){

        eventRecyclerView = view.findViewById(R.id.calendarRV)

        eventRecyclerView.layoutManager = LinearLayoutManager(view.context)

        eventList.add(EventModel("8:00 - 10:00", "Husk fodbold", "Fodboldgolf", false))
        eventList.add(EventModel("12:00 - 13:30", "Afholdes i kantinen", "Bestyrelsesmøde", true))
        eventList.add(EventModel("14:00 - 15:30", "Lokale 101", "Generélforsamling", false))
        eventList.add(EventModel("20:00 - 21:30", "Træneren holder fri", "Fodboldtræning", true))

        adapter = CalendarEventAdapter(eventList)

        eventRecyclerView.adapter = adapter
    }


}