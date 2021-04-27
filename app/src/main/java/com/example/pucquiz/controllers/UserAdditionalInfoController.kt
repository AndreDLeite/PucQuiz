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

    //TODO: Change it to be a factory of medals
    private fun generateAllMedals(): List<Medals> {
        val medalList = mutableListOf<Medals>()
        MedalsType.values().forEach {
            when (it) {
                MedalsType.ONE_QUIZ_ANSWERED -> {
                    medalList.add(
                        Medals(
                            name = "Primeiros Passos",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.FIVE_QUIZ_ANSWERED -> {
                    medalList.add(
                        Medals(
                            name = "Médios Passos",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.TEN_QUIZ_ANSWERED -> {
                    medalList.add(
                        Medals(
                            name = "Passo Largo",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.HND_SCORE_REACHED -> {
                    medalList.add(
                        Medals(
                            name = "Pequeno Progresso",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.THF_SCORE_REACHED -> {
                    medalList.add(
                        Medals(
                            name = "Médio Progresso",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.FH_SCORE_REACHED -> {
                    medalList.add(
                        Medals(
                            name = "Grande Progresso",
                            description = "", // TODO: Add description to each medal
                            type = it
                        )
                    )
                }
                MedalsType.FIRST_RANKING_POSITION -> {
                    medalList.add(
                        Medals(
                            name = "Maioral",
                            description = "", // TODO: Add description to each medal
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