package dk.bkskjold.nemsport.UI

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dk.bkskjold.nemsport.FragmentContainerActivity
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.ClubModel
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.PitchModel
import dk.bkskjold.nemsport.Models.UserModel
import dk.bkskjold.nemsport.R
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : AppCompatActivity() {

    val now = Calendar.getInstance()
    private var chosenDate = Date()

    var thisDate = Timestamp(Calendar.getInstance().getTime())

    private var pitches: MutableList<PitchModel> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val createEvent: Button = findViewById(R.id.createBtn)
        val backBtn: Button = findViewById(R.id.backBtn)
        val picktime: ImageView = findViewById(R.id.datePicker)
        val teams = ArrayList<String>()
        val teamsSpinner: Spinner = findViewById(R.id.spinner)
        val descView : EditText = findViewById<EditText>(R.id.descEt)


        var query = DatabaseHelper.db.collection("pitches")


        lifecycleScope.launch {
            pitches = DatabaseHelper.getPitchesFromDB()
            for (clubnames in pitches){
                teams.add(clubnames.pitchNames)
            }
            val apapter: ArrayAdapter<String> = ArrayAdapter<String>(
                baseContext, android.R.layout.simple_spinner_item, teams
            )
            teamsSpinner.adapter = apapter
        }


        picktime.setOnClickListener{
            // https://www.youtube.com/watch?v=gollUUFBKQA&ab_channel=CodeAndroid

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                findViewById<TextView>(R.id.show_date).text = dayOfMonth.toString()+"-"+(month.toInt()+1).toString()+"-"+year.toString()
                chosenDate = Date(year-1900, month,dayOfMonth, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND))


            }
            ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE))

            /*
            val time = Calendar.getInstance()
            val timePicker = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> }
            , time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),true)
            timePicker.show()
             */
            datePicker.show()


        }

        //teamsSpinner.getSelectedItem().toString()
        createEvent.setOnClickListener{
            DatabaseHelper.createEventInDB(EventModel(findViewById<EditText>(R.id.teamNameEt).text.toString()
                , Timestamp(chosenDate)
                , descView.text.toString()
                ,teamsSpinner.getSelectedItem().toString()
                , Firebase.auth.currentUser!!.uid.toString()
            ))
            startActivity(Intent(this,FragmentContainerActivity::class.java))
        }

        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }

    }

    }
