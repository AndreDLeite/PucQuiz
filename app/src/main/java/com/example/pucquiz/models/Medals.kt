package com.example.pucquiz.models

data class Medals(
    val name: String = "",
    val description: String = "",
    val type: MedalsType = MedalsType.UNKNOWN,
    val isActive: Boolean = false
)

enum class MedalsType {
    ONE_QUIZ_ANSWERED,
    FIVE_QUIZ_ANSWERED,
    TEN_QUIZ_ANSWERED,
    HND_SCORE_REACHED,
    THF_SCORE_REACHED,
    FH_SCORE_REACHED,
    FIRST_RANKING_POSITION,
    UNKNOWN
}
