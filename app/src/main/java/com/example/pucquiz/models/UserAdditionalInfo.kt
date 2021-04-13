package com.example.pucquiz.models

import androidx.annotation.NonNull

data class UserAdditionalInfo(
    val userName: String = "",
    val questionsAnswered: Int = 0,
    val userScore: Int = 0
) {
    fun getUserInitials(): String {
        return userName.split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> acc + s }
    }
}
