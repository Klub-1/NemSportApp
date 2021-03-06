package dk.bkskjold.nemsport.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.bkskjold.nemsport.R

class ParticipantsEventAdapter(private val _participants: List<String>) : RecyclerView.Adapter<ParticipantsEventAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.participant_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val participant = _participants[position]

        // sets the text to the textview from our itemHolder class
        holder.participantsView.text = participant.toString()
    }


    // Returns the number of participants
    override fun getItemCount(): Int {
            return _participants.size

    }


    // Holds the views for participant textView
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val participantsView: TextView = itemView.findViewById(R.id.participantTextView)

    }
}