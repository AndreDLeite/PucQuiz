package com.example.pucquiz.repositories.firebasertdb

import android.util.Log
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_INFO_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_MEDALS_BUCKET
import com.example.pucquiz.shared.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRTDBRepository : IFirebaseRTDBRepository {
    private val firebaseRTDBInstance = FirebaseDatabase.getInstance()

    override suspend fun fetchAllUsersAdditionalInfo(callback: FirebaseUserAddInfoCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET).get()
            .addOnCompleteListener { task ->
                val response = UsersRankingResponse()
                if (task.isSuccessful) {
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

    override suspend fun fetchUserInfoByUserId(userId: String, callback: FirebaseUserCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_BUCKET)
            .child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)
                        Log.d("user", "$userProfile")
                        userProfile?.let {
                            callback.onResponse(userProfile)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.onResponse(null)
                    }

                }
            )
    }

    override suspend fun fetchUserAdditionalInfoByUserId(
        userId: String,
        callback: FirebaseUserAddInfoCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET).child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userAdditionalInfo = snapshot.getValue(UserAdditionalInfo::class.java)
                    Log.d("user addinfo", "$userAdditionalInfo")
                    userAdditionalInfo?.let {
                        callback.onResponse(UsersRankingResponse(listOf(userAdditionalInfo)))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onResponse(UsersRankingResponse(listOf()))
                }

            }
        )
    }

    override suspend fun fetchUserMedalsByUserId(
        userId: String,
        callback: FirebaseUserMedalsCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_MEDALS_BUCKET)
            .child(userId).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userMedals = snapshot.getValue(UserMedals::class.java)
                    Log.d("user medals", "$userMedals")
                    callback.onResponse(userMedals)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onResponse(null)
                }

            }
        )
    }
}