package dk.bkskjold.nemsport.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.R
import java.text.SimpleDateFormat
import java.util.*

class TodayEventAdapter(private val eventList: List<EventModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // create new views
    // https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
    // Solution by: @Gil Goldzweig
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.event_item -> ViewHolderItem(inflater.inflate(viewType, parent, false))

            R.layout.event_item_divider -> ViewHolderDivider(inflater.inflate(viewType, parent, false))

            else -> throw IllegalArgumentException("Unsupported layout") // in case populated with a model we don't know how to display.
        }
    }

    // Holds the views for adding it to image and text
    class ViewHolderItem(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeView: TextView = itemView.findViewById(R.id.eventTimeTxt)
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
    }


    class ViewHolderDivider(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val titleView: TextView = itemView.findViewById(R.id.eventTitleTxt)
    }

    // https://stackoverflow.com/questions/58968541/kotlin-recyclerview-with-2-view-types
    // Solution by: @Gil Goldzweig
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder) {
            is ViewHolderItem -> {
                // bind NameViewHolder
                val event = eventList[position]

                val sdf = SimpleDateFormat("dd-MM-yyyy - HH:mm:ss")

                // sets the text to the textview from our itemHolder class
                holder.timeView.text = sdf.format(event.eventTime.toDate())

                holder.titleView.text = event.eventName.toString()
            }

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

        return when (element.pitches) {
            "TopSecret" -> R.layout.event_item_divider // Only TopSecret will be displayed as a title
            // TODO: Fix this hack
            else -> R.layout.event_item // All others
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}