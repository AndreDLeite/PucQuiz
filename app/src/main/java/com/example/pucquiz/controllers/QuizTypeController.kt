package com.example.pucquiz.controllers

import com.example.pucquiz.ui.shared.enums.QuizType

class QuizTypeController {

    fun quizTypeToString(currentType: QuizType): String {
        return when(currentType) {
            QuizType.TEACHER -> {
                "Pessoal"
            }
            QuizType.ENADE -> {
                "ENADE"
            }
            QuizType.UNKNOWN -> {
                ""
            }
        }
    }
}