package dk.bkskjold.nemsport.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import dk.bkskjold.nemsport.UI.EventActivity
import java.text.SimpleDateFormat


class TodayEventAdapter(private val eventList: List<EventModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    // https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
    // Solution by: @Gil Goldzweig
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context) // Inflate layout

        // Switchcase to handle the view type
        // Will load different viewholder Object for depending on if it is a divider
        return when (viewType) {
            R.layout.all_event_item -> ViewHolderItem(inflater.inflate(viewType, parent, false))

            R.layout.event_item_divider -> ViewHolderDivider(inflater.inflate(viewType, parent, false))

            else -> throw IllegalArgumentException("Unsupported layout") // in case populated with a model we don't know how to display.
        }
    }

    // Holds the views for adding it to text  and description
    // Standard Event Item
    class ViewHolderItem(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeView: TextView = itemView.findViewById(R.id.eventTimeTxt)
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
        val descView: TextView = itemView.findViewById(R.id.eventDescTxt)
        val cardView: CardView = itemView.findViewById(R.id.eventCard)
    }

    // Dividier Item
    class ViewHolderDivider(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
    }

    // https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
    // Solution by: @Gil Goldzweig
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // Switchcase to handle the view type
        when (holder) {
            is ViewHolderItem -> {
                // bind NameViewHolder
                val event = eventList[position]

                val sdf = SimpleDateFormat("dd-MM-yyyy - HH:mm:ss")

                // sets the text to the text
                holder.timeView.text = sdf.format(event.eventTime.toDate())
                holder.titleView.text = event.eventName.toString()
                holder.descView.text = event.eventDescription.toString()

                // Set listener to make event in list clickable
                holder.cardView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, EventActivity::class.java)
                    intent.putExtra("event",event)
                    holder.itemView.context.startActivity(intent)
                }
            }

            // Divider item. Is not clickable
            is ViewHolderDivider -> {
                val event = eventList[position]
                holder.titleView.text = event.eventName.toString()
            }
        }
    }
    // https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
    // Solution by: @Gil Goldzweig
    override fun getItemViewType(position: Int): Int {
        val element = eventList[position] // assuming your list is called "elements"

        // Switch case to that checks the pitches string.
        // If it matches it is a divider
        return when (element.pitches) {
            "TopSecret" -> R.layout.event_item_divider // Only TopSecret will be displayed as a title
            // TODO: Fix this hack
            else -> R.layout.all_event_item // All others
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}