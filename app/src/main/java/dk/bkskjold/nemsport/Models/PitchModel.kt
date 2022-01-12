package dk.bkskjold.nemsport.Models

import android.location.Location
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.protobuf.DescriptorProtos
import java.lang.ref.Reference

//this class is used to store the data from the firebase database
data class PitchModel(val pitchClub: DocumentReference? = null , val pitchID: Int = 0, val pitchLocation: GeoPoint? = null, val pitchNames:String = "")
