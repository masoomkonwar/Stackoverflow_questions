package com.masoom.buildforbharat.repository

import com.masoom.buildforbharat.API.RetrofitInstance

class QuestionsRepository {
    suspend fun getAllQuestions(order: String, sort : String) =
        RetrofitInstance.api.getAllQuestions( order ,sort )
}