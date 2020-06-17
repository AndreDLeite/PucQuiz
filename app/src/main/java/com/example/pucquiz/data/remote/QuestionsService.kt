package com.example.pucquiz.data.remote

import com.example.pucquiz.models.QuestionItem
import retrofit2.Call
import retrofit2.http.*

interface QuestionsService {

    @GET("questions")
    fun getQuestionsList(): Call<List<QuestionItem>>
}