package dk.bkskjold.nemsport.Helper

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Models.EventModel
import kotlinx.coroutines.tasks.await


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

    suspend fun getEventsFromDB(): MutableList<EventModel> {

        val snapshot = db
            .collection("eventsDebug")
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