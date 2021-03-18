package com.example.pucquiz.models

data class User(
    val name: String,
    val age: Int,
    val email: String,
    val period: Int,
    val registered_grades: List<Grade>
)
