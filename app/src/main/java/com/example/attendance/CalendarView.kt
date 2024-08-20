package com.example.attendance

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.HashSet

class CalendarView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    // Calendar components
    private lateinit var btnPrev: ImageView
    private lateinit var btnNext: ImageView
    private lateinit var txtDisplayDate: TextView
    private lateinit var txtDateYear: TextView
    private lateinit var gridView: GridView
    private lateinit var btn_cancel: CardView
    private lateinit var btn_add: CardView
    private lateinit var week_grid: GridView
    private lateinit var listner: OnItemClickListener

    private lateinit var selectedDate: Date

    interface OnItemClickListener {
        fun onClickItem(selectedDate: String , isCancel : Boolean)
    }
    private var mycalendar: Calendar = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH,titletext.a2.date)
        set(Calendar.MONTH,titletext.a2.month)
        set(Calendar.YEAR,titletext.a2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().year)
    }
    private val DAYS_COUNT = 42
    private lateinit var gestureDetector: GestureDetectorCompat

    object titletext{
        var title_yr:String = ""
        var tyy:Int = 0
        var saved_date_txt:String = ""
        var a2:Date = Calendar.getInstance().time
    }

    init {
        initControl(context, attrs)
        updateCalendar(HashSet()) // Ensure calendar is updated with today's date as selected
        weekupdate()
        btn_cancel = findViewById(R.id.btn_cancel)
        btn_add = findViewById(R.id.btn_add)

        btn_cancel.setOnClickListener {
            titletext.a2 = Calendar.getInstance().time
        listner.onClickItem("",true)

              mycalendar  = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH,titletext.a2.date)
                set(Calendar.MONTH,titletext.a2.month)
                set(Calendar.YEAR,titletext.a2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().year)
            }
        }
        btn_add.setOnClickListener {
            listner.onClickItem(titletext.saved_date_txt ,true)
//            txt1.text = titletext.selecteddate

        }
    }

     fun initView(listener: OnItemClickListener) {
        this.listner = listener
    }

    fun dialogdismiss(dialog: AlertDialog,txt1:TextView){

    }
    fun getWeekOfYear(date: Date): Int {
        // Convert the Date object to a ZonedDateTime
        val zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault())

        // Get the WeekFields for the default locale
        val weekFields = WeekFields.of(Locale.getDefault())

        // Get the week of the year
        return zonedDateTime.get(weekFields.weekOfWeekBasedYear())
    }

    fun assignUiElements() {
        // Layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button)
        btnNext = findViewById(R.id.calendar_next_button)
        txtDateYear = findViewById(R.id.date_display_year)
        txtDisplayDate = findViewById(R.id.date_display_date)
        gridView = findViewById(R.id.calendar_grid)
        btn_cancel = findViewById(R.id.btn_cancel)
        btn_add = findViewById(R.id.btn_add)
        week_grid=findViewById(R.id.week_grid)



        week_grid.setOnItemClickListener { _, _, position, _ ->
            val weekn = week_grid.adapter.getItem(position) as Date
            Log.e("000",weekn.toString())
//            Log.e("11111111",((weekn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfYear)/7).plus(1).toString())
        }


        gridView.setOnItemClickListener { _, _, position, _ ->
            // Get the date from the adapter
                selectedDate = gridView.adapter.getItem(position) as Date
                if (selectedDate.after(Calendar.getInstance().time)) {
//                Toast.makeText(context, "Future Date Not Available", Toast.LENGTH_SHORT).show()
                    return@setOnItemClickListener
                } else {
                    // Update the adapter with the selected date
                    (gridView.adapter as CalendarAdapter).setSelectedDate(selectedDate)
                    // Show toast message with the date
                    val sdf = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                    val dateToday = sdf.format(selectedDate)
                    titletext.a2 = selectedDate
                    titletext.saved_date_txt = dateToday
                    listner.onClickItem(titletext.saved_date_txt ,false)
                    mycalendar.set(Calendar.MONTH,selectedDate.month)
                    updateCalendar(HashSet())
                    weekupdate()
//                    Log.e("000000",((selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().dayOfYear)/7).toString())
                }
            }


//        gridView.setOnTouchListener()

        txtDisplayDate.setOnClickListener {
            showMonthSelectionDialog()
        }

        btnPrev.setOnClickListener {
            mycalendar.add(Calendar.MONTH, -1)
            updateCalendar(HashSet())
            weekupdate()
        }

        btnNext.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            if (mycalendar.get(Calendar.MONTH) >= cal.get(Calendar.MONTH) && mycalendar.get(Calendar.YEAR) >= cal.get(Calendar.YEAR)){
//                Toast.makeText(context, "Future Month Not Available", Toast.LENGTH_SHORT).show()
            }else {
                mycalendar.add(Calendar.MONTH, 1)
                updateCalendar(HashSet())
                weekupdate()
            }
        }



        // Set today's day of the week to blue

        gestureDetector = GestureDetectorCompat(context, GestureListener())
    }

    fun weekcolor(){
        val calendarToday = Calendar.getInstance()
        val todayWeekday = calendarToday.get(Calendar.DAY_OF_WEEK)

        // Find the TextView for each day of the week
        val weekDays = mapOf(
            Calendar.MONDAY to findViewById<TextView>(R.id.monday),
            Calendar.TUESDAY to findViewById<TextView>(R.id.tuesday),
            Calendar.WEDNESDAY to findViewById<TextView>(R.id.wednesday),
            Calendar.THURSDAY to findViewById<TextView>(R.id.thursday),
            Calendar.FRIDAY to findViewById<TextView>(R.id.friday),
            Calendar.SATURDAY to findViewById<TextView>(R.id.saturday),
            Calendar.SUNDAY to findViewById<TextView>(R.id.sunday)
        )

        // This Sets only First letter of Day in Calendar(eg. S for Sun)
        for (i in 1..7) {
            weekDays[i]?.text =
                weekDays[i]?.text?.get(0).toString()
        }

        if (mycalendar.get(Calendar.MONTH)== calendarToday.get(Calendar.MONTH) && mycalendar.get(Calendar.YEAR)== calendarToday.get(Calendar.YEAR)) {
            weekDays[todayWeekday]?.setTextColor(getResources().getColor(R.color.accent))
        }
        else
        {
            weekDays[todayWeekday]?.setTextColor(getResources().getColor(R.color.white))
        }
    }

    private fun initControl(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.custom_calendar_view, this)
        assignUiElements()
    }

    fun showMonthSelectionDialog()  {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.month_selector_dialog, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.apply {
            setGravity(Gravity.BOTTOM)
        }
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )


        val recy = dialogView.findViewById<RecyclerView>(R.id.recy)
        val title_year = dialogView.findViewById<TextView>(R.id.title_year)
        val year_prev = dialogView.findViewById<ImageView>(R.id.year_prev_button)
        val year_next = dialogView.findViewById<ImageView>(R.id.year_next_button)
        val cal: Calendar = Calendar.getInstance()

        fun tyr(){
            title_year.text = mycalendar.get(Calendar.YEAR).toString()
            titletext.title_yr = mycalendar.get(Calendar.YEAR).toString()
            titletext.tyy = mycalendar.get(Calendar.YEAR)
            if(mycalendar.get(Calendar.YEAR) >= cal.get(Calendar.YEAR)){
                year_next.setImageResource(R.drawable.disable_arrow_right_24)
            }
            else{
                year_next.setImageResource(R.drawable.baseline_keyboard_arrow_right_24)
            }
        }
        fun monthdialogupdate(){
            recy.adapter = MonthAdapter(months) { monthIndex ->
                mycalendar.set(Calendar.MONTH, monthIndex)
                title_year.text = mycalendar.get(Calendar.YEAR).toString()
                updateCalendar(HashSet())
                weekupdate()
                dialog.dismiss()
            }
        }
        tyr()
//        title_year.text = txtDateYear.text
//        titletext.title_yr = txtDateYear.text.toString()
        year_prev.setOnClickListener {
            mycalendar.add(Calendar.YEAR, -1)
            monthdialogupdate()
            tyr()
        }
        year_next.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            if (mycalendar.get(Calendar.YEAR) >= cal.get(Calendar.YEAR)){
//                Toast.makeText(context, "Future Year Not Available", Toast.LENGTH_SHORT).show()
            }
            else {
                mycalendar.add(Calendar.YEAR, 1)
                monthdialogupdate()
                tyr()
            }
        }
//        recy.layoutManager = GridLayoutManager(context, 3)
        monthdialogupdate()
        dialog.show()
    }
    fun weekupdate(){
        val cells = ArrayList<Date>()
        val calendar = mycalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        val monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2

        // Move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)
        while (cells.size < 6) {
            cells.add(calendar.time)
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
        week_grid.adapter = WeekAdapter(context,cells)
    }


    fun updateCalendar(events: HashSet<Date>) {
        val cells = ArrayList<Date>()
        val calendar = mycalendar.clone() as Calendar

        // Determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 0)
        val monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 2

        // Move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)

        // Fill cells
        while (cells.size < DAYS_COUNT) {
            cells.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Update grid
        val adapter = CalendarAdapter(context, mycalendar, cells, events)
        gridView.adapter = adapter
//        val adapter2 = WeekAdapter(context, mycalendar, cells, events)
//        week_grid.adapter = adapter2

        // Set today's date as selected date
        if(titletext.a2 == null) {
            adapter.setSelectedDate(Calendar.getInstance().time)
        }
        else{
            adapter.setSelectedDate(titletext.a2)
        }

        // Update title
        val sdf = SimpleDateFormat("EEEE,MMMM, yyyy", Locale.getDefault())
        val dateToday = sdf.format(mycalendar.time).split(",")
        txtDisplayDate.text = dateToday[1]
        txtDateYear.text = dateToday[2]
        val cal: Calendar = Calendar.getInstance()
        if(mycalendar.get(Calendar.MONTH) >= cal.get(Calendar.MONTH) && mycalendar.get(Calendar.YEAR) >= cal.get(Calendar.YEAR)){
            btnNext.setImageResource(R.drawable.disable_arrow_right_24)
        }
        else{
            btnNext.setImageResource(R.drawable.baseline_keyboard_arrow_right_24)
        }
        weekcolor()
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null || e2 == null) return false
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }
    }

    private fun onSwipeRight() {
        mycalendar.add(Calendar.MONTH, -1)
        updateCalendar(HashSet())
        weekupdate()
    }

    private fun onSwipeLeft() {
        val cal: Calendar = Calendar.getInstance()
        if (mycalendar.get(Calendar.MONTH) >= cal.get(Calendar.MONTH) && mycalendar.get(Calendar.YEAR) >= cal.get(Calendar.YEAR)){
//            Toast.makeText(context, "Future Month Not Available", Toast.LENGTH_SHORT).show()
        }else {
            mycalendar.add(Calendar.MONTH, 1)
            updateCalendar(HashSet())
            weekupdate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onInterceptTouchEvent(event)
    }
}
