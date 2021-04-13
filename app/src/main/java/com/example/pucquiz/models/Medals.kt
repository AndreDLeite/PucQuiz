package com.example.pucquiz.models

data class Medals(
    val name: String = "",
    val type: MedalsType,
    val isActive: Boolean = false
)

enum class MedalsType {
    QUESTIONS_ANSWERED,
    SCORE_REACHED,
    RANKING_POSITION,
    DAYS_IN_A_ROW
}
