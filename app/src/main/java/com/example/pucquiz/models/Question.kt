package com.example.pucquiz.models

data class Question(
    val teacherId: String = "",
    val summary: String = "",
    val answers: List<Answer> = listOf(),
    val questionType: GradeEnum = GradeEnum.UNKOWN
)
