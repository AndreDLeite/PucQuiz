package com.example.pucquiz.callbacks

import com.example.pucquiz.models.QuestionAdditionalInfo

interface QuestionsAdditionalInfoCallback {

    fun onResponse(questionsAdditionalInfo: List<QuestionAdditionalInfo>?)

}