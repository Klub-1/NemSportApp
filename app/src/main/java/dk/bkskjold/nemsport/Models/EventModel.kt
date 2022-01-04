package dk.bkskjold.nemsport.Models

import java.sql.Struct
import com.google.firebase.Timestamp

class EventModel(
    var eventName: String?  = null,
    var eventTime: Timestamp? = null
)