package com.example.pucquiz.callbacks

import com.example.pucquiz.callbacks.models.UserAdditionalInfoResponse

interface FirebaseUserAddInfoUpdateCallback {
    fun onResponse(result: UserAdditionalInfoResponse)
}