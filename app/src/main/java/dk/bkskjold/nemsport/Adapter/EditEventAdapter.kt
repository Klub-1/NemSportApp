package dk.bkskjold.nemsport.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import dk.bkskjold.nemsport.UI.event.CreateEventActivity
import java.text.SimpleDateFormat

class EditEventAdapter(private val eventList: List<EventModel>) : RecyclerView.Adapter<EditEventAdapter.ViewHolder>() {

    
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

        val event:EventModel = eventList[position]

        holder.cardEvent.setOnClickListener {
            val intent = Intent(holder.itemView.context,CreateEventActivity::class.java)
            // Tell CreateEventActivity to update the event
            intent.putExtra("update", true)
            // Event to be updated
            intent.putExtra("event",event)
            holder.itemView.context.startActivity(intent)
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy - HH:mm:ss")

        // sets the text to the textview from our itemHolder class
        holder.timeView.text =  sdf.format(event.eventTime.toDate())
        holder.descView.text = event.eventDescription.toString()
        holder.titleView.text = event.eventName.toString()

        // Remove imageview and textview, because user is the creator of the event
        holder.imageView.visibility = View.GONE
        holder.acceptView.visibility = View.GONE
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
        val cardEvent: CardView = itemView.findViewById(R.id.eventCard)
    }
}