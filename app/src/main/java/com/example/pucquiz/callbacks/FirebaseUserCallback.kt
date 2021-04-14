package com.example.pucquiz.callbacks

import com.example.pucquiz.models.User

interface FirebaseUserCallback {
    fun onResponse(response: User?)
}