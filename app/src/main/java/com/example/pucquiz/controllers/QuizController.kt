package com.example.pucquiz.controllers

import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoUpdateCallback
import com.example.pucquiz.callbacks.OperationCallback
import com.example.pucquiz.callbacks.models.GenericCallback
import com.example.pucquiz.callbacks.models.UserAdditionalInfoResponse
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class QuizController(private val firebaseRepo: IFirebaseRTDBRepository) {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    fun updateUserInfo(amountCorrect: Int, userScore: Int, quizGrade: GradeEnum, callback: OperationCallback) {
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
        }
    }

    private fun updateUserAdditionalInfo(userId: String, questionsAnswered: UserAdditionalInfo, callback: OperationCallback) {
        ioScope.launch {
            firebaseRepo.updateUserQuestionsAnswered(userId, questionsAnswered, object :
                FirebaseUserAddInfoUpdateCallback {
                override fun onResponse(result: UserAdditionalInfoResponse) {
                    if (result.operationResult) {
                        callback.callbackResponse(GenericCallback("Success", true))
                    } else {
                        callback.callbackResponse(GenericCallback("Erro ao atualizar dados da conta. Por favor, tente mais tarde.", false))
                    }
                }
            })
        }
    }
}