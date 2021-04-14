package com.example.pucquiz.controllers

import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.Medals
import com.example.pucquiz.models.MedalsType
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals

class UserAdditionalInfoController {

    fun createUserAdditionalInfo(user: User): UserAdditionalInfo {
        return UserAdditionalInfo(
            userName = user.name,
            questionsAnswered = generateUserGradeAnswers(user),
            userScore = 0
        )
    }

    private fun generateUserGradeAnswers(user: User): List<GradesAnswers> {
        val resultList = mutableListOf<GradesAnswers>()
        user.registered_grades.forEach {
            resultList.add(
                GradesAnswers(
                    userName = user.name,
                    correctQuantity = 0,
                    totalAnswered = 0,
                    gradeType = it.gradeType
                )
            )
        }
        return resultList
    }

    fun createUserMedals(user: User): UserMedals {
        return UserMedals(
            name = user.name,
            medals = generateAllMedals()
        )
    }

    private fun generateAllMedals(): List<Medals> {
        val medalList = mutableListOf<Medals>()
        MedalsType.values().forEach {
            when (it) {
                MedalsType.QUESTIONS_ANSWERED -> {
                    medalList.add(
                        Medals(
                            name = "X Questionarios repondidos!",
                            type = it
                        )
                    )
                }
                MedalsType.SCORE_REACHED -> {
                    medalList.add(
                        Medals(
                            name = "Pontuação X atingida!",
                            type = it
                        )
                    )
                }
                MedalsType.RANKING_POSITION -> {
                    medalList.add(
                        Medals(
                            name = "Ranking X atingido!",
                            type = it
                        )
                    )
                }
                MedalsType.DAYS_IN_A_ROW -> {
                    medalList.add(
                        Medals(
                            name = "X Dias seguidos atingido!",
                            type = it
                        )
                    )
                }
                MedalsType.UNKNOWN -> {
                    //ignore
                }
            }
        }
        return medalList
    }
}