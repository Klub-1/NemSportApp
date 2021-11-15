package dk.bkskjold.nemsport.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R

class TomorrowEventAdapter(private val mList: List<EventModel>) : RecyclerView.Adapter<TomorrowEventAdapter.ViewHolder>() {
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

        val event_model = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.timeView.text = event_model.time

        holder.titleView.text = event_model.title

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeView: TextView = itemView.findViewById(R.id.eventTimeTxt)
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
    }

}