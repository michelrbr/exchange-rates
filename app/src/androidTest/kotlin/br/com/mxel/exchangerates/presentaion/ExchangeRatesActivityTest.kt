package br.com.mxel.exchangerates.presentaion

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.mxel.exchangerates.R
import br.com.mxel.exchangerates.presentation.rates.ExchangeActivity
import br.com.mxel.exchangerates.presentation.util.EspressoIdlingResource
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeRatesActivityTest {

    @Before
     fun registerIdlingResource() {
         IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
     }

     @After
     fun unregisterIdlingResource() {
         IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
     }

    @Test
    fun showExchangeRatesTest() {

        ActivityScenario.launch(ExchangeActivity::class.java)

        onView(withId(R.id.loading)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rates_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.base_currency_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.base_currency_text_view)).check(matches(not(withText(""))))
        onView(withId(R.id.error_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.error_text_view)).check(matches(withText("")))
    }
}