package com.example.pucquiz.models

data class Question(
    val teacherId: String = "",
    val summary: String = "",
    var answers: List<Answer> = listOf(),
    val questionType: GradeEnum = GradeEnum.UNKOWN
)
