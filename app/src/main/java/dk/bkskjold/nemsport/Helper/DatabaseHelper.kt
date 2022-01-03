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
import dk.bkskjold.nemsport.Models.EventModel
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

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


    fun getFromDB(collection : String, args: ArrayList<ArrayList<String>>?): ArrayList<Any> {

        var query = db.collection(collection)

        var models: ArrayList<Any> = ArrayList()

        if (args != null) {
            for (arg in args){
                query = query.whereEqualTo(arg[0], arg[1]) as CollectionReference
            }
        }

        query.get().addOnSuccessListener {
            result ->
            for (document in result) {
                Log.w("DBHelper", document.data["eventName"].toString())
            }
        }.addOnFailureListener { Log.e("DBHelper", "FUCKING FEJL") }

        return ArrayList()
    }

    /*fun updateEventAttendanceInDB(userID:String, attending:Boolean, event:EventModel){
        var query = firebase.firestore().collection("events")
        query = query.where(id, "==", event.id)
        event.attending.userID.attending
    }*/

}