package com.example.pucquiz.callbacks

import com.example.pucquiz.models.UserMedals

interface FirebaseUserMedalsCallback {
    fun onResponse(userMedals: UserMedals?)
}