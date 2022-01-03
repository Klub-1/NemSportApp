package dk.bkskjold.nemsport.Models

import java.sql.Struct
import com.google.firebase.Timestamp

data class EventModel(val eventDeadline: com.google.firebase.Timestamp , val eventName: String ,val eventParticipantsUID: String,val evnetPitch: PitchModel ,val eventTime:com.google.firebase.Timestamp){

}
