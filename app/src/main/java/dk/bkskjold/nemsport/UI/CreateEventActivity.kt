package dk.bkskjold.nemsport.UI

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import dk.bkskjold.nemsport.Models.UserModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : AppCompatActivity() {

    val now = Calendar.getInstance()
    private var chosenDate = Date()

    private var pitches: MutableList<PitchModel> = mutableListOf()

    private lateinit var createEvent: Button
    private lateinit var backBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var picktime: ImageView
    private lateinit var descView: EditText
    private lateinit var teamName: EditText
    private lateinit var showDateTXT: TextView
    private lateinit var pitchSpinner: Spinner
    private lateinit var timeSpinner: Spinner

    private var pitchList: ArrayList<String> = ArrayList<String>()
    private var timeToPick: ArrayList<String> = ArrayList<String>()

    private val sdf: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        createEvent = findViewById(R.id.createBtn)
        backBtn = findViewById(R.id.backBtn)
        deleteBtn = findViewById(R.id.deleteBtn)
        picktime = findViewById(R.id.datePicker)
        descView = findViewById(R.id.descEt)
        teamName = findViewById(R.id.teamNameEt)
        showDateTXT = findViewById(R.id.show_date)
        pitchSpinner = findViewById(R.id.spinner)
        timeSpinner = findViewById(R.id.timeSpinner)

        showDateTXT.text = sdf.format(Date())

        val update: Boolean = intent.getBooleanExtra("update", false)

        if (update) {
            deleteBtn.visibility = View.VISIBLE

            var event: EventModel = intent.extras?.get("event") as EventModel
            if (event != null) {

                lifecycleScope.launch {
                    pitches = DatabaseHelper.getPitchesFromDB()
                    for (pitchNames in pitches) {
                        pitchList.add(pitchNames.pitchNames)
                    }
                    val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            baseContext, android.R.layout.simple_spinner_item, pitchList
                    )
                    pitchSpinner?.adapter = apapter
                }

                lifecycleScope.launch {
                    val chosenDateStart = Date(chosenDate.year, chosenDate.month, chosenDate.date, 0, 0, 0)
                    val chosenDateEnd = Date(chosenDate.year, chosenDate.month, chosenDate.date, 24, 0, 0)
                    timeToPick = arrayListOf()
                    for (i in 7..22) {
                        timeToPick.add(i.toString())
                    }

                    val eventsOnDay = DatabaseHelper.getEventsByDateFromDB(chosenDateStart, chosenDateEnd)
                    for (eventOnDay in eventsOnDay) {
                        if (eventOnDay.pitches == pitchSpinner?.selectedItem.toString()) {

                            timeToPick.remove(eventOnDay.eventTime.toDate().hours.toString())

                        }
                    }
                    val hour = event.eventTime.toDate().hours.toString()
                    if (timeToPick.contains(hour)) {
                        timeToPick.remove(hour)
                    }
                    timeToPick.add(0, hour)
                    val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            baseContext, android.R.layout.simple_spinner_item, timeToPick
                    )
                    timeSpinner?.adapter = apapter


                }

                deleteBtn.setOnClickListener {
                    DatabaseHelper.deleteEventInDB(event)
                            .addOnSuccessListener { startActivity(Intent(this, FragmentContainerActivity::class.java)) }
                            .addOnFailureListener { Toast.makeText(this, getString(R.string.fail_delete), Toast.LENGTH_LONG).show() }
                }

                handleCreateOrUpdate(update, event)

            }


        } else {
            //teamsSpinner.getSelectedItem().toString()
            deleteBtn.visibility = View.GONE

            lifecycleScope.launch {
                pitches = DatabaseHelper.getPitchesFromDB()
                for (pitchNames in pitches) {
                    pitchList.add(pitchNames.pitchNames)
                }
                val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        baseContext, android.R.layout.simple_spinner_item, pitchList
                )
                pitchSpinner?.adapter = apapter
            }

            createTimeSpinner()

            createEvent.setOnClickListener {
                chosenDate.hours = timeSpinner?.selectedItem.toString().toInt()
                chosenDate.minutes = 0
                chosenDate.seconds = 0
                DatabaseHelper.createEventInDB(EventModel(teamName.text.toString(), Timestamp(chosenDate), descView.text.toString(), pitchSpinner?.getSelectedItem().toString(), Firebase.auth.currentUser!!.uid.toString(), ArrayList<String>(), Firebase.database.reference.child("events").push().key!!
                ))
                startActivity(Intent(this, FragmentContainerActivity::class.java))
            }
        }

        picktime.setOnClickListener {
            // https://www.youtube.com/watch?v=gollUUFBKQA&ab_channel=CodeAndroid

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                chosenDate = Date(year - 1900, month, dayOfMonth, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND))
                showDateTXT.text = sdf.format(chosenDate) //dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
                createTimeSpinner()
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE))

            datePicker.show()

        }


        backBtn.setOnClickListener {
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }

    }

    private fun handleCreateOrUpdate(update: Boolean, event: EventModel) {
        createEvent.setText(getText(R.string.update_event))
        teamName.setText(event.eventName)
        descView.setText(event.eventDescription)
        pitchSpinner.setSelection(pitchList.indexOf(event.pitches))

        chosenDate = event.eventTime.toDate()

        val eventTime = event.eventTime.toDate()
        showDateTXT.text = sdf.format(eventTime)

        createEvent.setOnClickListener {
            chosenDate.hours = timeSpinner.selectedItem.toString().toInt()
            chosenDate.minutes = 0
            chosenDate.seconds = 0
            event.eventName = teamName.text.toString()
            event.eventDescription = descView.text.toString()
            event.pitches = pitchSpinner.selectedItem.toString()
            event.eventTime = Timestamp(chosenDate)

            DatabaseHelper.updateEventInDB(event)
                    .addOnSuccessListener { startActivity(Intent(this, FragmentContainerActivity::class.java)) }
                    .addOnFailureListener { Toast.makeText(this, getString(R.string.update_fail), Toast.LENGTH_LONG).show() }

        }
    }

    fun createTimeSpinner() {

        lifecycleScope.launch {
            val chosenDateStart = Date(chosenDate.year, chosenDate.month, chosenDate.date, 0, 0, 0)
            val chosenDateEnd = Date(chosenDate.year, chosenDate.month, chosenDate.date, 24, 0, 0)
            timeToPick = arrayListOf()
            for (i in 7..22) {
                timeToPick.add(i.toString())
            }

            val eventsOnDay = DatabaseHelper.getEventsByDateFromDB(chosenDateStart, chosenDateEnd)
            for (eventOnDay in eventsOnDay) {
                if (eventOnDay.pitches == pitchSpinner?.selectedItem.toString()) {
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
