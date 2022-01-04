package dk.bkskjold.nemsport.UI

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.Timestamp
import dk.bkskjold.nemsport.Helper.DatabaseHelper
import dk.bkskjold.nemsport.Models.EventModel
import dk.bkskjold.nemsport.Models.PitchModel
import dk.bkskjold.nemsport.R
import java.util.*
import kotlin.collections.ArrayList

class CreateEventActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)
        val createEvent: Button = findViewById(R.id.createBtn)
        val backBtn: Button = findViewById(R.id.backBtn)
        val picktime: ImageView = findViewById(R.id.datePicker)






        picktime.setOnClickListener{
            // https://www.youtube.com/watch?v=gollUUFBKQA&ab_channel=CodeAndroid
            val now = Calendar.getInstance()
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                Toast.makeText(this,now.toString(),Toast.LENGTH_LONG)
                findViewById<TextView>(R.id.show_date).text = dayOfMonth.toString()+"-"+(month.toInt()+1).toString()+"-"+year.toString()
            }
            ,now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DATE))


            val time = Calendar.getInstance()
            val timePicker = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> }
            , time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),true)
            timePicker.show()
            datePicker.show()


        }
        createEvent.setOnClickListener{
            DatabaseHelper.createEventInDB(EventModel(findViewById<EditText>(R.id.teamNameEt).text.toString(), Timestamp(Calendar.getInstance().getTime())))
            super.onBackPressed()
        }

        backBtn.setOnClickListener{
            // https://stackoverflow.com/questions/4038479/android-go-back-to-previous-activity
            super.onBackPressed()
        }

    }

    }
