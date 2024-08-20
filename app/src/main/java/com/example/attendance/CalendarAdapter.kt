package com.example.attendance

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.time.ZoneId
import java.util.*

class CalendarAdapter(
    context: Context,
    private val currentDate: Calendar,
    days: ArrayList<Date>,
    private val eventDays: HashSet<Date>
) : ArrayAdapter<Date>(context, R.layout.custom_calendar_day, days) {
    // For view inflation
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var selectedDate: Date = CalendarView.titletext.a2


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        // Day in question
        val calendar = Calendar.getInstance()
        val fulldate = calendar.time
        val date = getItem(position)

        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val weekday = calendar.get(Calendar.DAY_OF_WEEK)


        // Today
        val today = Date()
        val calendarToday = Calendar.getInstance()
        calendarToday.time = today

        // Inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.custom_calendar_day, parent, false)

        // Clear styling
        val textView = view as TextView
        textView.setTypeface(Typeface.SERIF, Typeface.BOLD)
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER
        textView.setBackgroundResource(0) // Clear background

        // Apply styles based on date conditions
        when {
            month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR) -> {
                // If this day is outside the currently selected month, grey it out
                textView.setTextColor(Color.parseColor("#FF8C8C8C"))
            }
            day == calendarToday.get(Calendar.DATE) && month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR) -> {
                // If it is today and in the selected month, set it to blue/bold
                if (date != selectedDate) todaybg(textView) else selectedbg(textView)
            }
            date == selectedDate || day == CalendarView.titletext.a2.date && month == CalendarView.titletext.a2.month && year == CalendarView.titletext.a2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().year-> {
                // If it is the selected date, set it to have a specific background
                selectedbg(textView)
            }
//            fulldate < currentDate.time->{
//                disabledate(textView)
//            }
            day > calendarToday.get(Calendar.DATE) && month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR) ->{
                disabledate(textView)
            }
            else -> {
                // Otherwise, it's a day in the current month, so keep it normal
                textView.setTextColor(Color.WHITE)
                textView.setTypeface(Typeface.SERIF, Typeface.BOLD)
            }
        }

        // Set text
        textView.text = day.toString()

        return view
    }

    fun setSelectedDate(date: Date) {
        selectedDate = date
        notifyDataSetChanged()
    }

    private fun selectedbg(view: View) {
        val textView = view as TextView
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER
        textView.setTypeface(Typeface.SERIF,Typeface.BOLD)
        textView.setBackgroundResource(R.drawable.selected_date_bg)
    }

    private fun todaybg(view: View) {
        val textView = view as TextView
        textView.setTextColor(Color.WHITE)
        textView.gravity = Gravity.CENTER
        textView.setTypeface(Typeface.SERIF,Typeface.BOLD)
        textView.setBackgroundResource(R.drawable.bg_today)
    }
    private fun disabledate(view: View) {
        val textView = view as TextView
        textView.setTextColor(Color.parseColor("#FF8C8C8C"))
        textView.gravity = Gravity.CENTER
    }
}