package dk.bkskjold.nemsport.Models

import java.sql.Struct
import com.google.firebase.Timestamp
import com.google.type.Date
import java.util.*

data class EventModel(val eventName: String = "",val eventTime:Timestamp = Timestamp(Calendar.getInstance().getTime())){

}
