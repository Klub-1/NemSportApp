package dk.bkskjold.nemsport.Helper

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.Models.EventModel
import java.util.*

object DatabaseHelper {

    @SuppressLint("StaticFieldLeak")
    var db: FirebaseFirestore = Firebase.firestore
    private lateinit var query: CollectionReference


    fun createEventInDB(event: EventModel) {
        db.collection("events").add(event).addOnSuccessListener { documentReference ->
            Log.d("DBHelper", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w("DBHelper", "Error adding document", e)
            }
    }


    fun getFromDB(collection : String, args: ArrayList<ArrayList<String>>?): QuerySnapshot? {


        var query = db.collection(collection)

        var document: QuerySnapshot? = null

        for (arg in args!!){
            query = query.whereEqualTo(arg[0], arg[1]) as CollectionReference
        }
        query.get()
            .addOnSuccessListener { result ->
            document = result
        }
            .addOnFailureListener { exception ->
            Log.w("BHelper getFromDB", "Error getting documents.", exception)
        }

        return document
    }

    fun updateEventAttendanceInDB(userID:String event:EventModel){
        
    }

}