package com.vishal.weatherapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.vishal.weather.kotlin.R
import com.vishal.weather.kotlin.WeatherActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test class for [WeatherActivity]. [android.support.test.espresso.Espresso] is
 * doing the instrumentation test.
 *
 * @author Vishal - 1st August 2019
 * @since 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class WeatherActivityTest {
    @Rule @JvmField
    var activityTestRule: ActivityTestRule<WeatherActivity> = ActivityTestRule(
        WeatherActivity::class.java
    )

    /**
     * Performs text updating and click listener on search view of [WeatherActivity]
     */
    @Test
    fun testSearchBox() {
        onView(withId(R.id.search_city)).perform(ViewActions.clearText())
        onView(withId(R.id.search_city)).perform(ViewActions.typeText("Delhi"))
        onView(withId(R.id.search_city)).perform(
            DrawableClickViewAction(
                DrawableClickViewAction.Right
            )
        )
        onView(withId(R.id.city_name)).check(matches(withText("Delhi")))
    }

    /**
     * Ensures the forecast view is populated.
     */
    @Test
    fun testRecyclerView() {
        onView(withId(R.id.search_city)).perform(
            DrawableClickViewAction(
                DrawableClickViewAction.Right
            )
        )

        onView(nthChildOf(withId(R.id.forecast), 0))
            .check(matches(hasDescendant(withText("Today"))))
        onView(nthChildOf(withId(R.id.forecast), 1))
            .check(matches(hasDescendant(withText("Tomorrow"))))

    }

    /**
     * Returns the view at childPosition of a [android.support.v7.widget.RecyclerView]
     *
     * @param parentMatcher view to be matched
     * @param childPosition position of the child
     */
    private fun nthChildOf(
        parentMatcher: Matcher<View>,
        childPosition: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Position is $childPosition")
            }

            public override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) {
                    return parentMatcher.matches(view.parent)
                }
                val group = view.parent as ViewGroup
                return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
            }
        }
    }
}