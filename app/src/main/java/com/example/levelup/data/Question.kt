package com.example.levelup.data

data class Question(
    val text: String,
    val answerChoices: List<String>,
    val correctAnswer: Int
)
