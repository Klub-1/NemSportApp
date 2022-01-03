package dk.bkskjold.nemsport.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R

class TodayEventAdapter(private val eventList: List<EventModel>) : RecyclerView.Adapter<TodayEventAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = eventList[position]

        // sets the text to the textview from our itemHolder class
        /*holder.timeView.text = event.eventTime.toString()

        holder.titleView.text = event.eventName.toString()*/
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return eventList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeView: TextView = itemView.findViewById(R.id.eventTimeTxt)
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
    }

}