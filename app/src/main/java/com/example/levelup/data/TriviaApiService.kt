package com.example.levelup.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService {
    @GET("https://opentdb.com/api.php")
    fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int = 17,
        @Query("difficulty") difficulty: String = "medium",
        @Query("type") type: String = "multiple"
    ): Response<TriviaResponse>
}