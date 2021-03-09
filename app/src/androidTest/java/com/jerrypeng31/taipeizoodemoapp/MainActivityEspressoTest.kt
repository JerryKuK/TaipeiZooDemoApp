package com.jerrypeng31.taipeizoodemoapp

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.base.IdlingResourceRegistry
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.jerrypeng31.taipeizoodemoapp.idling.Idling
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.MainActivity
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter.AreaItemAdapter
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter.ZooItemAdapter
import com.jerrypeng31.taipeizoodemoapp.retrofit_utils.AndroidTestUtils
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest{

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun zooFragment_isLoadAreaData(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().register(Idling.idlingResource)
        onView(withText("臺灣動物區")).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(Idling.idlingResource)
    }

    @Test
    fun areaFragment_isLoadAreaData(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().register(Idling.idlingResource)
        onView(withId(R.id.recyclerView_zoo))
            .perform(RecyclerViewActions.scrollToPosition<ZooItemAdapter.VH>(10),
                RecyclerViewActions.actionOnItemAtPosition<ZooItemAdapter.VH>(10, click()))

        onView(withText("植物資料")).check(matches(isDisplayed()))
        onView(withText("尤加利")).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(Idling.idlingResource)
    }

    @Test
    fun plantFragment_isGoToLastFragment(){
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().register(Idling.idlingResource)
        onView(withId(R.id.recyclerView_zoo))
            .perform(RecyclerViewActions.scrollToPosition<ZooItemAdapter.VH>(10),
                RecyclerViewActions.actionOnItemAtPosition<ZooItemAdapter.VH>(10, click()))

        onView(withId(R.id.recyclerView_area))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2),
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2, click()))

        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("尤加利"))))
        onView(withId(R.id.imageView_fullIcon)).check(matches(isDisplayed()))
        onView(withId(R.id.textView_content)).check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(Idling.idlingResource)
    }
}