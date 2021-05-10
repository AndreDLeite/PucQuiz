package com.example.pucquiz.controllers

import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoUpdateCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.callbacks.OperationCallback
import com.example.pucquiz.callbacks.models.GenericCallback
import com.example.pucquiz.callbacks.models.UserAdditionalInfoResponse
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.Medals
import com.example.pucquiz.models.MedalsType
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuizController(private val firebaseRepo: IFirebaseRTDBRepository) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    //region UserAdditionalInfo
    fun updateUserInfo(
        amountCorrect: Int,
        userScore: Int,
        quizGrade: GradeEnum,
        callback: OperationCallback
    ) {
        ioScope.launch {
            FirebaseAuth.getInstance().currentUser?.uid?.let { itUserId ->
                firebaseRepo.fetchUserAdditionalInfoByUserId(itUserId, object :
                    FirebaseUserAddInfoCallback {
                    override fun onResponse(response: UsersRankingResponse) {
                        response.usersAdditionalInfoList?.first()?.let {
                            val serverUserInfo = it
                            generateNewUserData(
                                itUserId,
                                serverUserInfo,
                                amountCorrect,
                                userScore,
                                quizGrade,
                                callback
                            )
                        }
                    }
                })

            }
        }
    }

    private fun generateNewUserData(
        userId: String,
        currentUserAdditionalInfo: UserAdditionalInfo,
        correctAmountAnswers: Int,
        userScore: Int,
        quizGrade: GradeEnum,
        callback: OperationCallback
    ) {
        val newGradeAnswerList = mutableListOf<GradesAnswers>()

        var newTotalAnswered: Int
        val realQuizGrade = GradeController().gradeToString(quizGrade)
        val changedGradeAnswer =
            currentUserAdditionalInfo.questionsAnswered.find { it.gradeType == realQuizGrade }

        newGradeAnswerList.addAll(currentUserAdditionalInfo.questionsAnswered)
        newGradeAnswerList.remove(changedGradeAnswer)

        changedGradeAnswer?.let { itGradeAnswer ->
            newTotalAnswered = itGradeAnswer.totalAnswered + 5
            val newCorrectQuantity =
                itGradeAnswer.correctQuantity + correctAmountAnswers
            val newUserScore = currentUserAdditionalInfo.userScore + userScore
            val newGradesAnswered = GradesAnswers(
                userName = currentUserAdditionalInfo.userName,
                correctQuantity = newCorrectQuantity,
                totalAnswered = newTotalAnswered,
                gradeType = changedGradeAnswer.gradeType
            )
            newGradeAnswerList.add(newGradesAnswered)
            val newValues = UserAdditionalInfo(
                userName = currentUserAdditionalInfo.userName,
                questionsAnswered = newGradeAnswerList,
                userScore = newUserScore
            )
            updateUserAdditionalInfo(userId, newValues, callback)
            fetchUserMedals(newValues, callback)
        }
    }

    private fun updateUserAdditionalInfo(
        userId: String,
        questionsAnswered: UserAdditionalInfo,
        callback: OperationCallback
    ) {
        ioScope.launch {
            firebaseRepo.updateUserQuestionsAnswered(userId, questionsAnswered, object :
                FirebaseUserAddInfoUpdateCallback {
                override fun onResponse(result: UserAdditionalInfoResponse) {
                    if (result.operationResult) {
                        callback.callbackResponse(GenericCallback("Success", true))
                    } else {
                        callback.callbackResponse(
                            GenericCallback(
                                "Erro ao atualizar dados da conta. Por favor, tente mais tarde.",
                                false
                            )
                        )
                    }
                }
            })
        }
    }

    //endregion

    //region UserMedals

    private fun fetchUserMedals(
        newUserAdditionalInfo: UserAdditionalInfo,
        callback: OperationCallback
    ) {
        ioScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""

            firebaseRepo.fetchUserMedalsByUserId(userId, object : FirebaseUserMedalsCallback {
                override fun onResponse(userMedals: UserMedals?) {
                    userMedals?.let {
                        generateNewUserMedalsValues(userId, it, newUserAdditionalInfo, callback)
                    } ?: run {
                        callback.callbackResponse(
                            GenericCallback(
                                "Erro ao atualizar suas medalhas. Por favor, tente mais tarde.",
                                false
                            )
                        )
                    }
                }
            })
        }
    }

    private fun generateNewUserMedalsValues(
        userId: String,
        userMedals: UserMedals,
        newUserAdditionalInfo: UserAdditionalInfo,
        callback: OperationCallback
    ) {
        val localMedals = userMedals.medals
        val earnedMedals = mutableListOf<Medals>()
        val newUserMedals = mutableListOf<Medals>()
        var hasUserDataChanged = false
        newUserMedals.addAll(localMedals)
        if (localMedals.all { it.medalIsActive }) {
            callback.callbackResponse(GenericCallback("Success", true))
        } else {
            localMedals.forEach {
                if (!it.medalIsActive) {
                    when (it.type) {
                        MedalsType.ONE_QUIZ_ANSWERED -> {
                            hasUserDataChanged = true
                            newUserMedals.remove(it)
                            earnedMedals.add(
                                Medals(
                                    name = it.name,
                                    description = it.description,
                                    type = it.type,
                                    medalIsActive = true
                                )
                            )
                            newUserAdditionalInfo.userScore += 50
                        }
                        MedalsType.FIVE_QUIZ_ANSWERED -> {
                            if (newUserAdditionalInfo.questionsAnswered.size >= 5) {
                                hasUserDataChanged = true
                                newUserMedals.remove(it)
                                earnedMedals.add(
                                    Medals(
                                        name = it.name,
                                        description = it.description,
                                        type = it.type,
                                        medalIsActive = true
                                    )
                                )
                                newUserAdditionalInfo.userScore += 50
                            }
                        }
                        MedalsType.TEN_QUIZ_ANSWERED -> {
                            if (newUserAdditionalInfo.questionsAnswered.size >= 10) {
                                hasUserDataChanged = true
                                newUserMedals.remove(it)
                                earnedMedals.add(
                                    Medals(
                                        name = it.name,
                                        description = it.description,
                                        type = it.type,
                                        medalIsActive = true
                                    )
                                )
                                newUserAdditionalInfo.userScore += 50
                            }
                        }
                        MedalsType.HND_SCORE_REACHED -> {
                            if (newUserAdditionalInfo.userScore >= 100) {
                                hasUserDataChanged = true
                                newUserMedals.remove(it)
                                earnedMedals.add(
                                    Medals(
                                        name = it.name,
                                        description = it.description,
                                        type = it.type,
                                        medalIsActive = true
                                    )
                                )
                                newUserAdditionalInfo.userScore += 50
                            }
                        }
                        MedalsType.THF_SCORE_REACHED -> {
                            if (newUserAdditionalInfo.userScore >= 250) {
                                hasUserDataChanged = true
                                newUserMedals.remove(it)
                                earnedMedals.add(
                                    Medals(
                                        name = it.name,
                                        description = it.description,
                                        type = it.type,
                                        medalIsActive = true
                                    )
                                )
                                newUserAdditionalInfo.userScore += 50
                            }
                        }
                        MedalsType.FH_SCORE_REACHED -> {
                            if (newUserAdditionalInfo.userScore >= 500) {
                                hasUserDataChanged = true
                                newUserMedals.remove(it)
                                earnedMedals.add(
                                    Medals(
                                        name = it.name,
                                        description = it.description,
                                        type = it.type,
                                        medalIsActive = true
                                    )
                                )
                                newUserAdditionalInfo.userScore += 50
                            }
                        }
                        MedalsType.UNKNOWN -> {
                            //ignore
                        }
                    }
                }
            }
            newUserMedals.addAll(earnedMedals)
            val newUserMedalsInfo = UserMedals(
                name = userMedals.name,
                medals = newUserMedals
            )

            updateUserMedals(userId, newUserMedalsInfo, callback)
            if(hasUserDataChanged) {
                updateUserAdditionalInfo(userId, newUserAdditionalInfo, callback)
            }
        }

    }

    private fun updateUserMedals(
        userId: String,
        newUserMedals: UserMedals,
        callback: OperationCallback
    ) {
        ioScope.launch {
            firebaseRepo.updateUserMedals(userId, newUserMedals, object :
                OperationCallback {
                override fun callbackResponse(operation: GenericCallback) {
                    operation.status?.let {
                        if (it) {
                            callback.callbackResponse(GenericCallback(operation.message, true))
                        } else {
                            callback.callbackResponse(GenericCallback(operation.message, false))
                        }
                    } ?: run {
                        callback.callbackResponse(GenericCallback(operation.message, false))
                    }
                }
            })
        }
    }
    //endregion
}