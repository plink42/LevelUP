package com.example.levelup

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.example.levelup.data.Question
import com.example.levelup.data.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizActivity : Activity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val triviaApiService = retrofit.create(TriviaApiService::class.java)
    private lateinit var questionText: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val intent = intent
        val category = intent.getIntExtra("category", 0)

        questionText = findViewById(R.id.question_text)
        answerButton1 = findViewById(R.id.answer_button_1)
        answerButton2 = findViewById(R.id.answer_button_2)
        answerButton3 = findViewById(R.id.answer_button_3)
        answerButton4 = findViewById(R.id.answer_button_4)

        val handler = Handler(Looper.getMainLooper())
        handler.post {
                displayQuiz(category)
        }
    }

    private fun displayQuiz(category: Int) {
        val questions = fetchAndShuffleQuestions(category)
        for (question in questions) {
            questionText.text = question.text
            answerButton1.text = question.answerChoices[0]
            answerButton2.text = question.answerChoices[1]
            answerButton3.text = question.answerChoices[2]
            answerButton4.text = question.answerChoices[3]
        }
    }

    private fun setupClickListeners() {
        answerButton1.setOnClickListener {
            handleAnswerClick(0)
        }
        answerButton2.setOnClickListener {
            handleAnswerClick(1)
        }
        answerButton3.setOnClickListener {
            handleAnswerClick(2)
        }
        answerButton4.setOnClickListener {
            handleAnswerClick(3)
        }
    }

    private fun handleAnswerClick(selectedAnswerIndex: Int) {
        // Check if the answer is correct
    }

    private fun loadQuestions(category: Int) : List<Question> {
        val response = triviaApiService.getQuestions(
            category = category
        )
        if (response.isSuccessful) {
            val questions = response.body()?.results?.map { result ->
                Question(
                    text = result.question,
                    answerChoices = result.incorrect + result.correct,
                    correctAnswer = result.incorrect.size
                )
            } ?: emptyList()
            return questions
        }
        return emptyList()
    }

    private fun shuffleQuestions(questions: List<Question>): List<Question> {
        val shuffledQuestions = questions.toMutableList()
        shuffledQuestions.shuffle()
        return shuffledQuestions
    }

    private fun fetchAndShuffleQuestions(category: Int) : List<Question> {
        val questions = loadQuestions(category)
        return shuffleQuestions(questions)
    }

}