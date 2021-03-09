package com.jerrypeng31.taipeizoodemoapp.mvvm.view

import android.os.Build
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.jerrypeng31.taipeizoodemoapp.R
import com.jerrypeng31.taipeizoodemoapp.mvvm.view.adapter.ZooItemAdapter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    lateinit var activity: MainActivity

    @Before
    fun setupActivity() {
        activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
    }

    @Test
    fun zooFragment_isLoadAreaData(){
        onView(withId(R.id.recyclerView_zoo)).check(matches(ViewMatchers.isDisplayed()))

        Thread.sleep(2000)
        
        onView(withText("臺灣動物區")).check(matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.recyclerView_zoo))
            .perform(RecyclerViewActions.scrollToPosition<ZooItemAdapter.VH>(16))

        onView(withText("生命驛站")).check(matches(ViewMatchers.isDisplayed()))
    }
}