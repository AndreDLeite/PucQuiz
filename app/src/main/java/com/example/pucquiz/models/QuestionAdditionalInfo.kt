package com.example.pucquiz.models

data class QuestionAdditionalInfo(
    val questionId: String = "",
    val timesAnswered: Int = 0,
    val answersAdditionalInfo: List<AnswerAdditionalInfo> = listOf()
)
