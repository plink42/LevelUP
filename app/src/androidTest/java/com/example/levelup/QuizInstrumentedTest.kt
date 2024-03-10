package com.example.levelup

import android.content.Intent
import android.os.SystemClock
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.levelup.data.QuizResult
import com.example.levelup.data.QuizStats
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizInstrumentedTest {
    @Rule
    @JvmField
    var quizActivityTestRule = ActivityScenarioRule(QuizActivity::class.java)


    @Test
    fun testQuizRunHistory() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), QuizActivity::class.java).apply {
            putExtra("category", 23)
            putExtra("difficulty", "easy")
        }

        quizActivityTestRule.scenario.onActivity { activity ->
            activity.startActivity(intent)
        }

        SystemClock.sleep(2000)
        onView(withId(R.id.question_category)).check(matches(withText("History")))

    }
}