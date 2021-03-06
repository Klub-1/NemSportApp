package dk.bkskjold.nemsport.UI

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dk.bkskjold.nemsport.Adapter.CalendarEventAdapter
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Helper.OnSwipeTouchListener
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import dk.bkskjold.nemsport.UI.edit_event.EventAdministrationActivity
import dk.bkskjold.nemsport.UI.event.CreateEventActivity
import dk.bkskjold.nemsport.databinding.FragmentCalendarBinding
import kotlinx.coroutines.launch
import java.util.*

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
    private lateinit var editEventFab: FloatingActionButton

    private lateinit var calendarViewSearch: CalendarView
    private lateinit var calendarToggleFL: FrameLayout

    private  lateinit var topViewLL: LinearLayout

    val now = Calendar.getInstance()

    var model = arrayListOf<EventModel>()

    /*
    RECYCLERVIEW
     */
    private lateinit var adapter: CalendarEventAdapter
    private var eventList: ArrayList<EventModel> = ArrayList<EventModel>()
    var query = DatabaseHelper.db.collection("events")


    private lateinit var eventAdapter: CalendarEventAdapter
    private var _binding: FragmentCalendarBinding? = null
    private var _eventList : MutableList<EventModel> = mutableListOf()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViews(root)
        
        val eventRecyclerView: RecyclerView = binding.calendarRV
        eventAdapter = CalendarEventAdapter(_eventList)
        eventRecyclerView.adapter = eventAdapter
        eventRecyclerView.layoutManager = LinearLayoutManager(activity, OrientationHelper.VERTICAL, false)

        //gets all the events on the current day and puts them in the recyclerview
        lifecycleScope.launch {
            _eventList += DatabaseHelper.getEventsByDateFromDB(Date(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),0,0)
                ,Date(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),23,59))
            eventAdapter.notifyDataSetChanged()
        }

        //when a date is clicked on the calendar, the events on that day are shown
        calendarViewSearch.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
            _eventList = mutableListOf()
            eventAdapter = CalendarEventAdapter(_eventList)
            eventRecyclerView.adapter = eventAdapter
            lifecycleScope.launch {
                _eventList += DatabaseHelper.getEventsByDateFromDB(Date(year-1900,month,dayOfMonth,0,0),(Date(year-1900,month,dayOfMonth,23,59)))
                eventAdapter.notifyDataSetChanged()
            }
        }
        fabHandler(root)
        hideCalendar(root)
        initViews(root)
        // Inflate the layout for this fragment
        return root
    }

    private fun initViews(view: View) {
        calendarViewSearch = view.findViewById(R.id.calendarViewSearch)
        calendarToggleFL = view.findViewById(R.id.calendarToggleFL)
        topViewLL = view.findViewById(R.id.topViewLL)

        menuFab = view.findViewById(R.id.menuFab)
        createFab = view.findViewById(R.id.createFab)
        editEventFab = view.findViewById(R.id.editEventFab)

    }

    private fun hideCalendar(view: View) {
        /*
        * TouchListener is based on
        * https://www.tutorialspoint.com/how-to-handle-swipe-gestures-in-kotlin
        */
        calendarToggleFL.setOnTouchListener(object : OnSwipeTouchListener(view.context) {
            override fun onSwipeUp() {
                super.onSwipeUp()
                calendarViewSearch.visibility = View.GONE
            }

            override fun onSwipeDown() {
                super.onSwipeDown()
                calendarViewSearch.visibility = View.VISIBLE
            }
        })
    }

    private fun fabHandler(view: View) {
        menuFab.setOnClickListener {
            // Hide and show floating action buttons
            if(FAB_IS_OPEN){
                menuFab.setImageResource(R.drawable.ic_action_menu_light)
                createFab.visibility = View.GONE
                editEventFab.visibility = View.GONE
                FAB_IS_OPEN = false
            }else{
                menuFab.setImageResource(R.drawable.ic_action_close_light)
                createFab.visibility = View.VISIBLE
                editEventFab.visibility = View.VISIBLE
                FAB_IS_OPEN = true
            }
        }

        createFab.setOnClickListener{
            startActivity(Intent(view.context, CreateEventActivity::class.java))
        }
        
        editEventFab.setOnClickListener{
            startActivity(Intent(view.context, EventAdministrationActivity::class.java))
        }
        
    }
    override fun onResume(){
        super.onResume()
        //wheen the fragment is resumed, the events on the current day are updated
        _eventList.clear()
        lifecycleScope.launch {
            _eventList.addAll( DatabaseHelper.getEventsByDateFromDB(Date(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),0,0)
                    ,Date(now.get(Calendar.YEAR)-1900,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),23,59)))
            eventAdapter.notifyDataSetChanged()
        }
        }
}