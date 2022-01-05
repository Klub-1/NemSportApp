package dk.bkskjold.nemsport.Models

import android.os.Parcel
import android.os.Parcelable
import java.sql.Struct
import com.google.firebase.Timestamp
import com.google.type.Date
import java.util.*
import java.io.Serializable
import kotlin.collections.ArrayList


//https://medium.com/@hgarg701/parcelable-in-android-using-kotlin-pass-object-from-one-activity-to-another-c34801d7ff03
data class EventModel(
    val eventName: String? = "",
    val eventTime:Timestamp = Timestamp(Calendar.getInstance().getTime()),
    val eventDescription:String = "",
    val pitches:String = "",
    val eventCreaterUID:String = "",
    val participants:ArrayList<String> = ArrayList(),
    val id: String = ""
)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Timestamp::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readArrayList(null) as ArrayList<String>
        ,parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventName)
        parcel.writeParcelable(eventTime, flags)
        parcel.writeString(eventDescription)
        parcel.writeString(pitches)
        parcel.writeString(eventCreaterUID)
        parcel.writeList(participants)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventModel> {
        override fun createFromParcel(parcel: Parcel): EventModel {
            return EventModel(parcel)
        }

        override fun newArray(size: Int): Array<EventModel?> {
            return arrayOfNulls(size)
        }
    }
}
