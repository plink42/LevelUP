package com.example.levelup.data

import com.google.gson.annotations.SerializedName

data class TriviaResponse(
    val results: List<TriviaQuestion>
)

data class TriviaQuestion(
    val category: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>,
    val question: String,
    val type: String
)
