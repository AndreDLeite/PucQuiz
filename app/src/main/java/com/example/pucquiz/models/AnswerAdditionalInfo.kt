package com.example.pucquiz.models

data class AnswerAdditionalInfo(
    val answerId: String = "",
    val correctAnswer: Boolean = false,
    val timesAnswered: Int = 0
)
