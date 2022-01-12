package dk.bkskjold.nemsport.Helper


import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs


/*
* THIS FILE IS DIRECTLY COPIED FROM
* https://www.tutorialspoint.com/how-to-handle-swipe-gestures-in-kotlin
*
* Comments made by us
*/

internal open class OnSwipeTouchListener(c: Context?) :
    OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD: Int = 100
        private val SWIPE_VELOCITY_THRESHOLD: Int = 100
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onClick()
            return super.onSingleTapUp(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onDoubleClick()
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            onLongClick()
            super.onLongPress(e)
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                // Swipe direction on y-axis
                val diffY = e2.y - e1.y
                // Swipe direction on x-axis
                val diffX = e2.x - e1.x

                // if abs(diffX) > abs(diffY) = swipe left or right
                // else = swipe up or down
                if (abs(diffX) > abs(diffY)) {
                    // Check if the swipe was big enough to be registered
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(
                            velocityX
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        // if diffX is positive = swipe right
                        // if diffX is negative (else) = swipe left
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                } else {
                    // Check if the swipe was big enough to be registered
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(
                            velocityY
                        ) > SWIPE_VELOCITY_THRESHOLD
                    ) {
                        // if diffY is positive = swipe up
                        // if diffY is negative (else) = swipe down
                        if (diffY < 0) {
                            onSwipeUp()
                        } else {
                            onSwipeDown()
                        }
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeUp() {}
    open fun onSwipeDown() {}
    private fun onClick() {}
    private fun onDoubleClick() {}
    private fun onLongClick() {}

    init {
        gestureDetector = GestureDetector(c, GestureListener())
    }
}
