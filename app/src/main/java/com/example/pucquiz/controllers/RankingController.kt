package com.example.pucquiz.controllers

import com.example.pucquiz.models.UserAdditionalInfo

class RankingController {

    fun orderUsersRanking(list: List<UserAdditionalInfo>): List<UserAdditionalInfo> {
        return list.sortedByDescending {
            it.userScore
        }
    }
}