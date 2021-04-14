package com.example.pucquiz.callbacks.models

import com.example.pucquiz.models.UserAdditionalInfo


data class UsersRankingResponse(
    var usersAdditionalInfoList: List<UserAdditionalInfo>? = null,
    var exception: Exception? = null
)
