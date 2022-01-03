package dk.bkskjold.nemsport.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R

class CalendarEventAdapter(private val eventList: List<EventModel>) : RecyclerView.Adapter<CalendarEventAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_event_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = eventList[position]

        // sets the text to the textview from our itemHolder class
        /*holder.timeView.text = event.eventTime.toString()

        holder.descView.text = event.eventName.toString()

        holder.titleView.text = event.eventName.toString()*/

        /*if (event.participate){
            holder.acceptView.visibility = View.VISIBLE
            holder.imageView.setBackgroundResource(R.drawable.circle_imageview_green)
            holder.imageView.setImageResource(R.drawable.ic_action_done_light)
        }else{
            holder.acceptView.visibility = View.GONE
            holder.imageView.setBackgroundResource(R.drawable.circle_imageview_red)
            holder.imageView.setImageResource(R.drawable.ic_action_user_light)
        }*/

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return eventList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeView: TextView = itemView.findViewById(R.id.eventTimeTxt)
        val descView: TextView = itemView.findViewById(R.id.eventDescTxt)
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
        val acceptView: TextView = itemView.findViewById(R.id.eventAcceptedTxt)
        val imageView: ImageView = itemView.findViewById(R.id.acceptedIv)
    }

}