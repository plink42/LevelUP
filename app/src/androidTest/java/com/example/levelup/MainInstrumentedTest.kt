package com.example.levelup

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matchers.hasToString
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {
    @Rule
    @JvmField
    var mainActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun selectHistory() {
        onView(withId(R.id.history_button)).perform(click())
        intended(allOf(hasComponent(QuizActivity::class.java.name), hasExtra("category", 23)))
    }

    @Test
    fun selectDifficultyAndComputers() {
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("easy"))).perform(click())
        onView(withId(R.id.computers_button)).perform(click())
        intended(allOf(hasComponent(QuizActivity::class.java.name), hasExtra("category", 18), hasExtra("difficulty", "easy")))
    }

    @Test
    fun selectDifficultyAndMythology() {
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("hard"))).perform(click())
        onView(withId(R.id.mythology_button)).perform(click())
        intended(allOf(hasComponent(QuizActivity::class.java.name), hasExtra("category", 20), hasExtra("difficulty", "hard")))
    }

    @Test
    fun selectDifficultyAndArt() {
        onView(withId(R.id.difficulty_spinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("medium"))).perform(click())
        onView(withId(R.id.art_button)).perform(click())
        intended(allOf(hasComponent(QuizActivity::class.java.name), hasExtra("category", 25), hasExtra("difficulty", "medium")))
    }
}