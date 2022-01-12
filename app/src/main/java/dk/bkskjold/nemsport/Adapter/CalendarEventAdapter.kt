package dk.bkskjold.nemsport.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import dk.bkskjold.nemsport.UI.event.EventActivity
import java.text.SimpleDateFormat

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
        val event:EventModel = eventList[position]

        //If the event card clicked the event activity wil be started
        holder.cardEvent.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventActivity::class.java)
            //adding the event to the intent
            intent.putExtra("event",event)
            holder.itemView.context.startActivity(intent)
        }

        // defining a SimpleDateFormat for the shown date
        val sdf = SimpleDateFormat("dd-MM-yyyy - HH:mm:ss")

        // sets the text to the textview from our itemHolder class
        holder.timeView.text =  sdf.format(event.eventTime.toDate())
        holder.descView.text = event.eventDescription.toString()
        holder.titleView.text = event.eventName.toString()

        // Checks if the logged in user is set as a participant in the event
        if (event.participants.contains(Firebase.auth.uid)){
            //makes sure the event is visible in the recyclerview
            holder.acceptView.visibility = View.VISIBLE 
            // sets image to 'participating' image
            holder.imageView.setBackgroundResource(R.drawable.circle_imageview_green)
            holder.imageView.setImageResource(R.drawable.ic_action_done_light)
        }else{
            // makes sure the event is not visible in the recyclerview
            holder.acceptView.visibility = View.GONE
            // sets image to 'not participating' image
            holder.imageView.setBackgroundResource(R.drawable.circle_imageview_red)
            holder.imageView.setImageResource(R.drawable.ic_action_user_light)
        }

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