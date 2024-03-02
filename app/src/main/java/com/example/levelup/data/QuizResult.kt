package com.example.levelup.data

data class QuizStats(
    var totalQuestions: Int,
    var correctAnswers: Int,
    var incorrectAnswers: Int,
    var difficulty: String,
    val results: List<QuizResult>
)

data class QuizResult(
    val question: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean
)
