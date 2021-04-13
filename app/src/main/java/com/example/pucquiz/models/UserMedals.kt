package com.example.pucquiz.models

data class UserMedals(
    val name: String = "",
    val medals: List<Medals> = emptyList()
)
