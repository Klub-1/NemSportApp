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
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : AppCompatActivity() {
    //

    val now = Calendar.getInstance()
    private var chosenDate = Date()

    private var pitches: MutableList<PitchModel> = mutableListOf()

    var timeSpinner: Spinner? = null
    var pitchSpinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val createEvent: Button = findViewById(R.id.createBtn)
        val backBtn: Button = findViewById(R.id.backBtn)
        val picktime: ImageView = findViewById(R.id.datePicker)
        val pitchList = ArrayList<String>()
        val descView : EditText = findViewById<EditText>(R.id.descEt)
        val teamName = findViewById<EditText>(R.id.teamNameEt)
        val showDateTXT = findViewById<TextView>(R.id.show_date)
        pitchSpinner = findViewById(dk.bkskjold.nemsport.R.id.spinner)
        timeSpinner  = findViewById(R.id.timeSpinner)


        showDateTXT.text = now.get(Calendar.DAY_OF_MONTH).toString() + "-" + (now.get(Calendar.MONTH)+1) +"-" + now.get(Calendar.YEAR)


        lifecycleScope.launch {
            pitches = DatabaseHelper.getPitchesFromDB()
            for (pitchNames in pitches){
                pitchList.add(pitchNames.pitchNames)
            }
            val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                baseContext, android.R.layout.simple_spinner_item, pitchList
            )
            pitchSpinner?.adapter = apapter
        }


        createTimeSpinner()


        picktime.setOnClickListener{
            // https://www.youtube.com/watch?v=gollUUFBKQA&ab_channel=CodeAndroid

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                showDateTXT.text = dayOfMonth.toString()+"-"+(month+1).toString()+"-"+year.toString()
                chosenDate = Date(year-1900, month,dayOfMonth, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND))
                createTimeSpinner()
            }
            ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE))

            datePicker.show()

        }

        //teamsSpinner.getSelectedItem().toString()
        createEvent.setOnClickListener{
            chosenDate.hours = timeSpinner?.selectedItem.toString().toInt()
            chosenDate.minutes = 0
            chosenDate.seconds = 0
            DatabaseHelper.createEventInDB(EventModel(findViewById<EditText>(R.id.teamNameEt).text.toString()
                , Timestamp(chosenDate)
                , descView.text.toString()
                ,pitchSpinner?.getSelectedItem().toString()
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
    fun createTimeSpinner(){

        lifecycleScope.launch {
            val chosenDateStart = Date(chosenDate.year,chosenDate.month,chosenDate.date,0,0,0)
            val chosenDateEnd = Date(chosenDate.year,chosenDate.month,chosenDate.date,24,0,0)
            val timeToPick:ArrayList<String> = arrayListOf()
            for(i in 7..22) {
                timeToPick.add(i.toString())
            }

            val eventsOnDay = DatabaseHelper.getEventsByDateFromDB(chosenDateStart,chosenDateEnd)
            for (eventOnDay in eventsOnDay){
                if(eventOnDay.pitches == pitchSpinner?.selectedItem.toString()){
                    timeToPick.remove(eventOnDay.eventTime.toDate().hours.toString())
                }
            }
            val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                baseContext, android.R.layout.simple_spinner_item, timeToPick
            )
            timeSpinner?.adapter = apapter
        }
    }

    }
