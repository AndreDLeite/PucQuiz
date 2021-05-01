package com.example.pucquiz.repositories.firebasertdb

import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback

interface IFirebaseRTDBRepository {

    suspend fun fetchAllUsersAdditionalInfo(callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserInfoByUserId(userId: String, callback: FirebaseUserCallback)

    suspend fun fetchUserAdditionalInfoByUserId(userId: String, callback: FirebaseUserAddInfoCallback)

    suspend fun fetchUserMedalsByUserId(userId: String, callback: FirebaseUserMedalsCallback)

    suspend fun fetchTeacherQuestions(userId: String, callback: FirebaseTeacherQuestionsCallback)
}