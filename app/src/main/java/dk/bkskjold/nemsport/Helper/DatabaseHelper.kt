package dk.bkskjold.nemsport.Helper

import android.annotation.SuppressLint
import android.util.Log
import com.firebase.ui.auth.data.model.User
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
import dk.bkskjold.nemsport.Models.UserModel
import kotlinx.coroutines.tasks.await
import java.util.*


object DatabaseHelper {

    @SuppressLint("StaticFieldLeak")
    var db: FirebaseFirestore = Firebase.firestore


    fun createEventInDB(event: EventModel) {
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

    suspend fun getUserFromDB(UID: String ): UserModel? {

        val docref = db
            .collection("users")
            .document(UID)

        val snapshot = docref
            .get()
            .await()

        return snapshot.toObject(UserModel::class.java)
    }

    fun updateUserInDB(UID: String, user: UserModel): Task<Void> {
        val userRef = db.collection("users").document(UID)

        return userRef.set(user)
    }

    fun updateEventInDB(event: EventModel): Task<Void> {
        val docRef = db.collection("events").document(event.id)

        return docRef.set(event)
    }
    
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

    suspend fun getEventsCreatedByUserFromDB(uid: String): MutableList<EventModel> {

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


    /*fun updateEventAttendanceInDB(userID:String, attending:Boolean, event:EventModel){
        var query = firebase.firestore().collection("events")
        query = query.where(id, "==", event.id)
        event.attending.userID.attending
    }*/

}