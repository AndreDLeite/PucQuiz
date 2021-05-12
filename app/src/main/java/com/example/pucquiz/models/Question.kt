package com.example.pucquiz.models

import com.example.pucquiz.ui.shared.enums.QuizType

data class Question(
    val id: String = "",
    val teacherId: String = "",
    val summary: String = "",
    var answers: List<Answer> = listOf(),
    val questionGrade: GradeEnum = GradeEnum.UNKOWN,
    val questionType: QuizType = QuizType.UNKNOWN
)
