package com.example.attendance

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Locale

class MonthAdapter(private val months: Array<String>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

        val calendar = Calendar.getInstance()

    inner class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthName: TextView = itemView.findViewById(R.id.text_monthname)

        fun bind(month: String, position: Int) {
            monthName.text = month
            val sdf = SimpleDateFormat("MMMM,YYYY", Locale.getDefault())
            val cal = Calendar.getInstance()
            val curtime = sdf.format(calendar.time).split(",")
            val curmonth = curtime[0]
            val curyear = curtime[1]
            if (month == curmonth && curyear == CalendarView.titletext.title_yr){
                itemView.setBackgroundResource(R.drawable.bg_month)
            }
//            val cal = Calendar.getInstance()
            val currentCalendarMonth = cal.get(Calendar.MONTH)
            val currentCalendarYear = cal.get(Calendar.YEAR)

            if (CalendarView.titletext.tyy > currentCalendarYear || (position > 5 && CalendarView.titletext.tyy == 2024)) {
                monthName.setTextColor(itemView.context.resources.getColor(R.color.disabled)) // Set grey color
                itemView.isClickable = false
            } else {
//                monthName.setTextColor(itemView.context.resources.getColor(R.color.black)) // Set default color
//                itemView.isClickable = true
                itemView.setOnClickListener {
                    onItemClick(position)
                }
            }
//            itemView.setOnClickListener {
//                onItemClick(position)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_list, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(months[position], position)
    }

    override fun getItemCount(): Int {
        return months.size
    }
}
