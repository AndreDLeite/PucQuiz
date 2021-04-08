package com.example.pucquiz.models

data class User(
    val name: String = "",
    val age: Int = -1,
    val email: String = "",
    val period: Int = -1,
    val registered_grades: List<Grade> = emptyList()
)