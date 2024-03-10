package com.example.levelup

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.levelup.data.Question
import com.example.levelup.data.QuizResult
import com.example.levelup.data.QuizStats
import com.example.levelup.data.TriviaApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizActivity : AppCompatActivity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://opentdb.com/api.php/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val triviaApiService = retrofit.create(TriviaApiService::class.java)
    private lateinit var questionText: TextView
    private lateinit var explanationText: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button
    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var results = mutableListOf<QuizResult>()
    private var stats = QuizStats(
        category = 0,
        categoryName = "",
        totalQuestions = 0,
        correctAnswers = 0,
        incorrectAnswers = 0,
        difficulty = "",
        results = results
    )
    private lateinit var timerText: TextView
    private var countDownTimer: CountDownTimer? = null

    private lateinit var questionNumber: TextView
    private lateinit var questionCategoryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val intent = intent
        val category = intent.getIntExtra("category", 0)
        val difficulty = intent.getStringExtra("difficulty").toString()

        questionText = findViewById(R.id.question_text)
        explanationText = findViewById(R.id.explanation_text)
        explanationText.visibility = TextView.INVISIBLE
        answerButton1 = findViewById(R.id.answer_button_1)
        answerButton2 = findViewById(R.id.answer_button_2)
        answerButton3 = findViewById(R.id.answer_button_3)
        answerButton4 = findViewById(R.id.answer_button_4)
        timerText = findViewById(R.id.timer)
        questionNumber = findViewById(R.id.question_number)
        questionCategoryText = findViewById(R.id.question_category)

        lifecycleScope.launch {
            displayQuiz(category, difficulty)
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerText.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                timerText.text = "0"
                handleTimeout()
            }
        }
        countDownTimer?.start()
    }

    private suspend fun displayQuiz(category: Int, difficulty: String) {
        questions = fetchAndShuffleQuestions(category, difficulty)
        stats.category = category
        stats.totalQuestions = questions.size
        stats.difficulty = difficulty
        displayNextQuestion()
    }

    private fun setupClickListeners(question: Question) {
        answerButton1.setOnClickListener {
            handleAnswerClick(0, answerButton1, question)
        }
        answerButton2.setOnClickListener {
            handleAnswerClick(1, answerButton2, question)
        }
        answerButton3.setOnClickListener {
            handleAnswerClick(2, answerButton3, question)
        }
        answerButton4.setOnClickListener {
            handleAnswerClick(3, answerButton4, question)
        }
    }

    private fun displayNextQuestion() {
        if (currentQuestionIndex >= questions.size) {
            displayResults()
        } else {
            val question = questions[currentQuestionIndex]
            explanationText.visibility = TextView.INVISIBLE
            questionText.text = Html.fromHtml(question.text, 0)
            answerButton1.text = Html.fromHtml(question.answerChoices[0], 0)
            answerButton1.setBackgroundColor(resources.getColor(R.color.purple_500, null))
            answerButton1.isClickable = true
            answerButton1.visibility = Button.VISIBLE
            answerButton2.text = Html.fromHtml(question.answerChoices[1], 0)
            answerButton2.setBackgroundColor(resources.getColor(R.color.purple_500, null))
            answerButton2.isClickable = true
            answerButton2.visibility = Button.VISIBLE
            answerButton3.text = Html.fromHtml(question.answerChoices[2], 0)
            answerButton3.setBackgroundColor(resources.getColor(R.color.purple_500, null))
            answerButton3.isClickable = true
            answerButton3.visibility = Button.VISIBLE
            answerButton4.text = Html.fromHtml(question.answerChoices[3], 0)
            answerButton4.setBackgroundColor(resources.getColor(R.color.purple_500, null))
            answerButton4.isClickable = true
            answerButton4.visibility = Button.VISIBLE
            val formattedQuestionNumber = String.format(
                resources.getString(R.string.question_number),
                currentQuestionIndex + 1,
                stats.totalQuestions
            )
            questionNumber.text = formattedQuestionNumber
            questionCategoryText.text = Html.fromHtml(question.category, 0)
            setupClickListeners(question)
            startTimer()
        }
    }
    private fun handleAnswerClick(selectedAnswerIndex: Int, button: Button, question: Question) {
        countDownTimer?.cancel()
        val selectedAnswer = question.answerChoices[selectedAnswerIndex]
        results.add(QuizResult(
            question = question.text,
            selectedAnswer = selectedAnswer,
            correctAnswer = question.correctAnswer,
            isCorrect = selectedAnswer == question.correctAnswer
        ))
        explanationText.visibility = TextView.VISIBLE
        explanationText.text = Html.fromHtml(question.explanation, 0)
        if (selectedAnswer == question.correctAnswer) {
            button.setBackgroundColor(resources.getColor(R.color.green_500, null))
            stats.correctAnswers += 1
            button.isClickable = false
            for (answerButton in listOf(answerButton1, answerButton2, answerButton3, answerButton4).filter { it != button }) {
                answerButton.setBackgroundColor(resources.getColor(R.color.red_500, null))
                answerButton.isClickable = false
                answerButton.visibility = Button.INVISIBLE
            }
        } else {
            button.setBackgroundColor(resources.getColor(R.color.red_500, null))
            stats.incorrectAnswers += 1
            for (answerButton in listOf(answerButton1, answerButton2, answerButton3, answerButton4).filter { it != button }) {
                if (answerButton.text == Html.fromHtml(question.correctAnswer, 0)) {
                    answerButton.setBackgroundColor(resources.getColor(R.color.green_500, null))
                    answerButton.isClickable = false
                } else {
                    answerButton.setBackgroundColor(resources.getColor(R.color.red_500, null))
                    answerButton.isClickable = false
                    if (answerButton.text != button.text) {
                        answerButton.visibility = Button.INVISIBLE
                    }
                }
            }
        }
        currentQuestionIndex++
        lifecycleScope.launch {
            delayAndDisplayNextQuestion()
        }
    }

    private fun handleTimeout() {
        stats.incorrectAnswers += 1
        currentQuestionIndex++
        lifecycleScope.launch {
            delayAndDisplayNextQuestion()
        }
    }

    private suspend fun delayAndDisplayNextQuestion() {
        delay(1500)
        displayNextQuestion()
    }

    private suspend fun loadQuestions(category: Int, difficulty: String) : List<Question> {
        val response = triviaApiService.getQuestions(
            category = category,
            difficulty = difficulty
        )
        if (response.isSuccessful) {
            Log.d("QuizActivity", "Response: ${response.body()?.results}")
            val questions = response.body()?.results?.map { result ->
                Question(
                    text = result.question,
                    answerChoices = shuffleAnswers(result.incorrectAnswers + result.correctAnswer),
                    correctAnswer = result.correctAnswer,
                    explanation = "This is a placeholder explanation because the API does not provide one. The answer is: ${result.correctAnswer}",
                    category = result.category
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

    private suspend fun fetchAndShuffleQuestions(category: Int, difficulty: String) : List<Question> {
        val questions = loadQuestions(category, difficulty)
        return shuffleQuestions(questions)
    }

    private fun shuffleAnswers(answers: List<String>): List<String> {
        val shuffledAnswers = answers.toMutableList()
        shuffledAnswers.shuffle()
        return shuffledAnswers
    }

    private fun displayResults() {
        // Display the results
        timerText.text = ""
        timerText.visibility = TextView.INVISIBLE
        explanationText.visibility = TextView.INVISIBLE
        questionText.setBackgroundColor(resources.getColor(R.color.light_green_700, null))
        val percentage = (stats.correctAnswers.toDouble() / stats.totalQuestions.toDouble()) * 100
        var congrats = "Good effort!"
        if (stats.correctAnswers == stats.totalQuestions) {
            congrats = "Perfect!"
        } else if (percentage >= 75) {
            congrats = "Great job!"
        }
        val resultText = Html.fromHtml("<h1>${percentage}%</h1>"+
                "<h2>${congrats}</h2>"+
                "You got <b>${stats.correctAnswers}</b> "+
                "out of <b>${stats.totalQuestions}</b> correct!<br><br>" +
                "Correct: <b>${stats.correctAnswers}</b><br>" +
                "Incorrect: <b>${stats.incorrectAnswers}</b><br>" +
                "Difficulty: <b>${stats.difficulty}</b>", 0)
        val qtLayout = questionText.layoutParams as ViewGroup.LayoutParams
        qtLayout.height = ViewGroup.LayoutParams.WRAP_CONTENT
        questionText.layoutParams = qtLayout
        questionText.text = resultText
        answerButton1.visibility = Button.VISIBLE
        answerButton1.text = resources.getText(R.string.main_menu)
        answerButton1.setBackgroundColor(resources.getColor(R.color.purple_500, null))
        answerButton1.setOnClickListener {
            mainMenu()
        }
        answerButton2.isClickable = false
        answerButton2.visibility = Button.INVISIBLE
        answerButton3.isClickable = false
        answerButton3.visibility = Button.INVISIBLE
        answerButton4.isClickable = false
        answerButton4.visibility = Button.INVISIBLE
        questionNumber.visibility = TextView.INVISIBLE
    }

    private fun mainMenu() {
        // Return to the main menu
        finish()
    }


}