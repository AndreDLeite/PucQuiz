package com.example.pucquiz.repositories.firebasertdb

import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoUpdateCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.UserAdditionalInfo

interface IFirebaseRTDBRepository {

    suspend fun fetchAllUsersAdditionalInfo(callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserInfoByUserId(userId: String, callback: FirebaseUserCallback)

    suspend fun fetchUserAdditionalInfoByUserId(userId: String, callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserMedalsByUserId(userId: String, callback: FirebaseUserMedalsCallback)

    suspend fun fetchTeacherQuestions(userId: String, callback: FirebaseTeacherQuestionsCallback)

    suspend fun fetchGradeQuestions(grade: GradeEnum, callback: FirebaseGradeQuestionsCallBack)

    suspend fun updateUserQuestionsAnswered(userId: String, newUserAdditionalInfo: UserAdditionalInfo, callback: FirebaseUserAddInfoUpdateCallback)
}