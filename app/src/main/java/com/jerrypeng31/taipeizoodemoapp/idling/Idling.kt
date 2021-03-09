package com.jerrypeng31.taipeizoodemoapp.idling

import androidx.test.espresso.idling.CountingIdlingResource

object Idling {
    val idlingResource = CountingIdlingResource("test")
}