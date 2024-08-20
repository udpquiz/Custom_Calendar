package com.example.attendance

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.GridView
import androidx.core.view.GestureDetectorCompat

interface OnSwipeListener {
    fun onSwipeRight()
    fun onSwipeLeft()
}

class SwipeableGridView(context: Context, attrs: AttributeSet?) : GridView(context, attrs) {
    private lateinit var gestureDetector: GestureDetectorCompat
    private var swipeListener: OnSwipeListener? = null

    init {
        gestureDetector = GestureDetectorCompat(context, GestureListener())
        if (context is OnSwipeListener) {
            swipeListener = context
        }
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
                        Log.d("SwipeableGridView", "Swipe Right Detected")
                        swipeListener?.onSwipeRight()
                    } else {
                        Log.d("SwipeableGridView", "Swipe Left Detected")
                        swipeListener?.onSwipeLeft()
                    }
                    return true
                }
            }
            return false
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performClick()
            return true
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onInterceptTouchEvent(event)
    }
}