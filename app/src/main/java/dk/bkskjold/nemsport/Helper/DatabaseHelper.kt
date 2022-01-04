package dk.bkskjold.nemsport.Helper

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Models.ClubModel
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.PitchModel
import kotlinx.coroutines.tasks.await
import java.util.*


object DatabaseHelper {

    @SuppressLint("StaticFieldLeak")
    var db: FirebaseFirestore = Firebase.firestore


    fun createEventInDB(event: EventModel) {
        db.collection("events").add(event).addOnSuccessListener { documentReference ->
            Log.d("DBHelper", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w("DBHelper", "Error adding document", e)
            }
    }

    suspend fun getEventsFromDB( ): MutableList<EventModel> {

        val snapshot = db
            .collection("events")
            .get()
            .await()

        return snapshot.toObjects(EventModel::class.java)
    }

    suspend fun getPitchesFromDB( ): MutableList<PitchModel> {

        val snapshot = db
            .collection("pitches")
            .get()
            .await()

        return snapshot.toObjects(PitchModel::class.java)
    }

    suspend fun getEventsByDateFromDB( dateStart:Date,dateEnd:Date): MutableList<EventModel> {

        val snapshot = db
            .collection("events")
            .orderBy("eventTime")
            .startAt(dateStart)
            .endAt(dateEnd)
            .get()
            .await()

        return snapshot.toObjects(EventModel::class.java)
    }



    /*fun updateEventAttendanceInDB(userID:String, attending:Boolean, event:EventModel){
        var query = firebase.firestore().collection("events")
        query = query.where(id, "==", event.id)
        event.attending.userID.attending
    }*/

}