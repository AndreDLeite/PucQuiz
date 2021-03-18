package com.example.pucquiz.models

data class User(
    val name: String,
    val age: Int,
    val email: String,
    val period: Int = 0,
    val registeredGrades: List<Grade>
)
