package com.example.levelup

import com.example.levelup.data.Question
import com.example.levelup.data.QuizResult
import com.example.levelup.data.QuizStats
import com.example.levelup.data.TriviaApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class QuizUnitTest {
    @ExperimentalCoroutinesApi
    @Test
    fun testApiCall() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/api.php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val triviaApiService = retrofit.create(TriviaApiService::class.java)
        withContext(testDispatcher) {
            val response = triviaApiService.getQuestions(
                category = 23,
                difficulty = "medium"
            )
            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertEquals(10, response.body()!!.results.size)
            assertEquals("History", response.body()!!.results[0].category)
        }
    }

    @Test
    fun testQuizStats() {
        val quizStats = QuizStats(
            23,
            "History",
            10,
            7,
            3,
            "easy",
            listOf(
                QuizResult("What is the capital of France?", "Paris", "Paris", true),
                QuizResult("What is the capital of Spain?", "Barcelona", "Madrid", false),
                QuizResult("What is the capital of Italy?", "Rome", "Rome", true),
                QuizResult("What is the capital of Germany?", "Berlin", "Berlin", true),
                QuizResult("What is the capital of Portugal?", "Lisbon", "Lisbon", true),
                QuizResult("What is the capital of Greece?", "Oil", "Athens", false),
                QuizResult("What is the capital of Egypt?", "Cairo", "Cairo", true),
                QuizResult("What is the capital of China?", "Beijing", "Beijing", true),
                QuizResult("What is the capital of Japan?", "Tokyo", "Tokyo", true),
                QuizResult("What is the capital of Australia?", "Sydney", "Canberra", false)
            )
        )
        assertEquals(23, quizStats.category)
        assertEquals("History", quizStats.categoryName)
        assertEquals(10, quizStats.totalQuestions)
        assertEquals(7, quizStats.correctAnswers)
        assertEquals(3, quizStats.incorrectAnswers)
        assertEquals("easy", quizStats.difficulty)
        assertEquals(10, quizStats.results.size)
    }

    @Test
    fun testQuizResult() {
        val quizResult = QuizResult("What is the capital of France?", "Paris", "Paris", true)
        assertEquals("What is the capital of France?", quizResult.question)
        assertEquals("Paris", quizResult.selectedAnswer)
        assertEquals("Paris", quizResult.correctAnswer)
        assertTrue(quizResult.isCorrect)
    }

    @Test
    fun testQuestion() {
        val question = Question(
            "What is the capital of France?",
            listOf("Paris", "London", "Berlin", "Madrid"),
            "Paris",
            "Paris is the capital of France.",
            "Geography"
        )
        assertEquals("What is the capital of France?", question.text)
        assertEquals(4, question.answerChoices.size)
        assertEquals("Paris", question.correctAnswer)
        assertEquals("Paris is the capital of France.", question.explanation)
        assertEquals("Geography", question.category)
    }
}