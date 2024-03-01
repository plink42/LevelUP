package com.example.levelup.data

data class TriviaResponse(
    val results: List<TriviaQuestion>
)

data class TriviaQuestion(
    val category: String,
    val correct: String,
    val incorrect: List<String>,
    val question: String,
    val type: String
)
