package com.example.pucquiz.repositories.firebasertdb

import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoUpdateCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.callbacks.OperationCallback
import com.example.pucquiz.callbacks.QuestionsAdditionalInfoCallback
import com.example.pucquiz.callbacks.SingleQuestionAddInfoCallback
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.Question
import com.example.pucquiz.models.QuestionAdditionalInfo
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.ui.shared.enums.QuizType

interface IFirebaseRTDBRepository {

    suspend fun fetchAllUsersAdditionalInfo(callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserInfoByUserId(userId: String, callback: FirebaseUserCallback)

    suspend fun fetchUserAdditionalInfoByUserId(userId: String, callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserMedalsByUserId(userId: String, callback: FirebaseUserMedalsCallback)

    suspend fun fetchTeacherQuestions(userId: String, callback: FirebaseTeacherQuestionsCallback)

    suspend fun fetchGradeQuestionsByQuestionType(grade: GradeEnum, questionType: QuizType, callback: FirebaseGradeQuestionsCallBack)

    suspend fun updateUserQuestionsAnswered(userId: String, newUserAdditionalInfo: UserAdditionalInfo, callback: FirebaseUserAddInfoUpdateCallback)

    suspend fun updateUserMedals(userId: String, newUserMedals: UserMedals, callback: OperationCallback)

    suspend fun fetchQuestionAdditionalInfo(callback: QuestionsAdditionalInfoCallback)

    suspend fun fetchQuestionAdditionalInfoByQuestionId(questionId: String, callback: SingleQuestionAddInfoCallback)

    suspend fun sendQuestionAddInfoToFirebase(questionId: String, questionAdditionalInfo: QuestionAdditionalInfo): Boolean
}