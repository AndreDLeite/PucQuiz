package com.example.pucquiz.callbacks

import com.example.pucquiz.models.QuestionAdditionalInfo

interface SingleQuestionAddInfoCallback {

    fun onResponse(data: QuestionAdditionalInfo?)
}