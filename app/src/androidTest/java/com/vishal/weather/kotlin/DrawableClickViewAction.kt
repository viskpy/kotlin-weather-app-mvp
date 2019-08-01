package com.vishal.weatherapp

import android.graphics.Point
import android.support.annotation.IntDef
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Custom [ViewAction] that detects a drawable(left, top, right, bottom) on any EditText.
 *
 * @author Vishal - 1st August 2019
 * @since 1.0.0
 */
class DrawableClickViewAction(
    @param:Location @field:Location
    private val drawableLocation: Int
) : ViewAction {
    companion object {
        const val Left = 0
        const val Top = 1
        const val Right = 2
        const val Bottom = 3
    }

    override fun getConstraints(): Matcher<View> {
        return allOf(isAssignableFrom(EditText::class.java), object : BoundedMatcher<View, EditText>(
            EditText::class.java
        ) {
            override fun describeTo(description: org.hamcrest.Description) {
                description.appendText("has drawable")
            }

            override fun matchesSafely(editText: EditText): Boolean {
                return editText.requestFocusFromTouch() && editText.compoundDrawables[drawableLocation] != null

            }
        })
    }

    override fun getDescription(): String {
        return "click drawable "
    }

    override fun perform(uiController: UiController, view: View) {
        val tv = view as EditText
        if (tv != null && tv.requestFocusFromTouch()) {
            val drawableBounds = tv.compoundDrawables[drawableLocation].bounds

            val clickPoint = arrayOfNulls<Point>(4)
            clickPoint[Left] =
                Point(tv.left + drawableBounds.width() / 2, (tv.pivotY + drawableBounds.height() / 2).toInt())
            clickPoint[Top] = Point(
                (tv.pivotX + drawableBounds.width() / 2).toInt(),
                tv.top + drawableBounds.height() / 2
            )
            clickPoint[Right] =
                Point(tv.right + drawableBounds.width() / 2, (tv.pivotY + drawableBounds.height() / 2).toInt())
            clickPoint[Bottom] = Point(
                (tv.pivotX + drawableBounds.width() / 2).toInt(),
                tv.bottom + drawableBounds.height() / 2
            )

            if (tv.dispatchTouchEvent(
                    MotionEvent.obtain(
                        android.os.SystemClock.uptimeMillis(),
                        android.os.SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN,
                        clickPoint[drawableLocation]!!.x.toFloat(),
                        clickPoint[drawableLocation]!!.y.toFloat(),
                        0
                    )
                )
            )
                tv.dispatchTouchEvent(
                    MotionEvent.obtain(
                        android.os.SystemClock.uptimeMillis(),
                        android.os.SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP,
                        clickPoint[drawableLocation]!!.x.toFloat(),
                        clickPoint[drawableLocation]!!.y.toFloat(),
                        0
                    )
                )
        }
    }

    @IntDef(Left, Top, Right, Bottom)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Location
}