package com.masoom.buildforbharat.API

import com.masoom.buildforbharat.models.QuestionResponse
import com.masoom.buildforbharat.utils.Constants.Companion.API_KEY
import com.masoom.buildforbharat.utils.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionsAPI {
    @GET("2.3/questions")
    suspend fun getAllQuestions(
        @Query("order")
        order : String,
        @Query("sort")
        sort : String,
        @Query("site")
        site : String = "stackoverflow",
        @Query("key")
        key: String = API_KEY,
        ) : Response<QuestionResponse>
}