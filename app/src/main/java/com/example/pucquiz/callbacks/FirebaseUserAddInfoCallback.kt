package com.example.pucquiz.callbacks

import com.example.pucquiz.callbacks.models.UsersRankingResponse


interface FirebaseUserAddInfoCallback {
    fun onResponse(response: UsersRankingResponse)
}