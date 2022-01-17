package dk.bkskjold.nemsport.Helper

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.PitchModel
import dk.bkskjold.nemsport.Models.UserModel
import kotlinx.coroutines.tasks.await
import java.util.*

// Kotlin Singleton creation
object DatabaseHelper {

    @SuppressLint("StaticFieldLeak")
    var db: FirebaseFirestore = Firebase.firestore

    /**
    * @param event Event to be created
    */
    fun createEventInDB(event: EventModel) {
        // Create event
        db.collection("events").document(event.id).set(event).addOnSuccessListener { documentReference ->
            Log.d("DBHelper", "DocumentSnapshot added with ID:")
        }
            .addOnFailureListener { e ->
                Log.w("DBHelper", "Error adding document", e)
            }
    }
    /**
     * @param user the usermodel
     * @param UID the document name
     */
    fun createUserInDB(UID:String,user: UserModel) {
        db.collection("users").document(UID).set(user).addOnSuccessListener{ documentReference ->
            Log.d("DBHelper", "DocumentSnapshot added with ID:")
        }
            .addOnFailureListener { e ->
                Log.w("DBHelper", "Error adding document", e)
            }


    }

    // Gets all events in database. 
    suspend fun getEventsFromDB( ): MutableList<EventModel> {
        
        val snapshot = db
            .collection("events") // Select collection called events
            .get()  // Fetch contents
            .await()  // Hold thread execution until fetched

        // Map all events in snapshot to our EventModel class
        return snapshot.toObjects(EventModel::class.java) 
    }

    suspend fun getPitchesFromDB( ): MutableList<PitchModel> {

        val snapshot = db
            .collection("pitches")
            .get()
            .await()

        return snapshot.toObjects(PitchModel::class.java)
    }

    suspend fun getUserFromDB(UID: String ): UserModel? {

        val docref = db
            .collection("users")
            .document(UID)

        val snapshot = docref
            .get()
            .await()

        return snapshot.toObject(UserModel::class.java)
    }

    // Takes a user as parameter and updates that user in the Firestore database 
    fun updateUserInDB(UID: String, user: UserModel): Task<Void> {
        val userRef = db.collection("users").document(UID)

        return userRef.set(user) // update command for Firestore
    }

    // Takes an event as parameter and updates that event in the Firestore database
    fun updateEventInDB(event: EventModel): Task<Void> {
        val docRef = db.collection("events").document(event.id)

        return docRef.set(event) // Update command for Firestore
    }
    
    // Deletes an event in Firestore database
    fun deleteEventInDB(event: EventModel): Task<Void> {
        val docRef = db.collection("events").document(event.id)        
        return docRef.delete()
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

    /**
    * @param uid UserID
    */
    suspend fun getEventsCreatedByUserFromDB(uid: String): MutableList<EventModel> {

        // Get events where eventCreatorUID = uid order by eventTime
        val snapshot = db
            .collection("events")
            .whereEqualTo("eventCreaterUID", uid)
            .orderBy("eventTime")            
            .get()
            .await()

        return snapshot.toObjects(EventModel::class.java)
    }
    
    suspend fun updateParticapents(event: EventModel){
        val snapshot = db
            .collection("events")
            .document(event.id)
            .set(event)
            .await()


    }
    fun getUserUIDFromDB() : String? {
        return Firebase.auth.currentUser?.uid
    }
}