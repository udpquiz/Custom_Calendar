package com.example.attendance

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var txt1: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val float_add: FloatingActionButton = findViewById(R.id.float_add)
        val float_add1: FloatingActionButton = findViewById(R.id.float_add1)
        txt1 = findViewById(R.id.txt1)
        float_add.setOnClickListener {
            showDialog()
        }
    }

    fun showDialog() {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.calendar_dialog, null)
        val calendarView = dialogView.findViewById<CalendarView>(R.id.customCalendarView)


//        val events = HashSet<Date>() // Add any event dates if needed
//        calendarView.updateCalendar(events)


        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        dialog.window?.apply {
            setGravity(Gravity.BOTTOM)
        }
//        calendarView.dialogdismiss(dialog,txt1)
        calendarView.initView(object : CalendarView.OnItemClickListener{
            override fun onClickItem(selectedDate: String , isCancel : Boolean) {
                txt1.text = selectedDate
                if(isCancel){
                    dialog.dismiss()
                }
            }

        })
        dialog.show()
    }
}
