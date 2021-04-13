package com.example.pucquiz.repositories.firebasertdb

import com.example.pucquiz.FirebaseCallback
import com.example.pucquiz.models.UserAdditionalInfo

interface IFirebaseRTDBRepository {

    fun fetchAllUsersAdditionalInfo(callback: FirebaseCallback)
}