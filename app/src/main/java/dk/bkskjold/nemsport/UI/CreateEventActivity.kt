package dk.bkskjold.nemsport.UI

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.PitchModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : AppCompatActivity() {

    val now = Calendar.getInstance()
    private var chosenDate = Date()

    private var pitches: MutableList<PitchModel> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val createEvent: Button = findViewById(R.id.createBtn)
        val backBtn: Button = findViewById(R.id.backBtn)
        val picktime: ImageView = findViewById(R.id.datePicker)
        val pitchList = ArrayList<String>()
        val pitchSpinner: Spinner = findViewById(R.id.spinner)
        val descView : EditText = findViewById<EditText>(R.id.descEt)
        val teamName = findViewById<EditText>(R.id.teamNameEt)
        val showDateTXT = findViewById<TextView>(R.id.show_date)





        lifecycleScope.launch {
            pitches = DatabaseHelper.getPitchesFromDB()
            for (pitchNames in pitches){
                pitchList.add(pitchNames.pitchNames)
            }
            val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                baseContext, android.R.layout.simple_spinner_item, pitchList
            )
            pitchSpinner.adapter = apapter
        }


        picktime.setOnClickListener{
            // https://www.youtube.com/watch?v=gollUUFBKQA&ab_channel=CodeAndroid

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                showDateTXT.text = dayOfMonth.toString()+"-"+(month+1).toString()+"-"+year.toString()
                chosenDate = Date(year-1900, month,dayOfMonth, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND))
            }
            ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE))

            datePicker.show()
        }

        //teamsSpinner.getSelectedItem().toString()
        createEvent.setOnClickListener{
            DatabaseHelper.createEventInDB(EventModel(teamName.text.toString()
                , Timestamp(chosenDate)
                , descView.text.toString()
                ,pitchSpinner.getSelectedItem().toString()
                , Firebase.auth.currentUser!!.uid.toString()
                , ArrayList<String>()
                , Firebase.database.reference.child("events").push().key!!
            ))
            startActivity(Intent(this,FragmentContainerActivity::class.java))
        }

        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }

    }

    }
