package com.example.pucquiz.models

import androidx.annotation.NonNull

data class UserAdditionalInfo(
    val userName: String = "",
    var questionsAnswered: List<GradesAnswers> = listOf(),
//    val questionsAnswered: Int = 0,//this is the size of the list of answered questions by type
    var userScore: Int = 0
) {
    fun getUserInitials(): String {
        return userName.split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> acc + s }
    }
}
