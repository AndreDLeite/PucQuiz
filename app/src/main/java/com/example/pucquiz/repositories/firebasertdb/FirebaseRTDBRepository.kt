package com.example.pucquiz.repositories.firebasertdb

import com.example.pucquiz.FirebaseCallback
import com.example.pucquiz.UsersRankingResponse
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_INFO_BUCKET
import com.google.firebase.database.FirebaseDatabase

class FirebaseRTDBRepository: IFirebaseRTDBRepository {
    private val firebaseRTDBInstance = FirebaseDatabase.getInstance()

    override fun fetchAllUsersAdditionalInfo(callback: FirebaseCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET).get().addOnCompleteListener { task ->
            val response = UsersRankingResponse()
            if(task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.usersAdditionalInfoList = result.children.map { snapShot ->
                        snapShot.getValue(UserAdditionalInfo::class.java)!!
                    }
                }
            } else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }
}