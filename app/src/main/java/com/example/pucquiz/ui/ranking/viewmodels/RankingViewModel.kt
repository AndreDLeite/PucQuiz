package com.example.pucquiz.ui.ranking.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.controllers.RankingController
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class RankingViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _usersRanking = MutableLiveData<Resource<List<UserAdditionalInfo>>>()
    val usersRanking: LiveData<Resource<List<UserAdditionalInfo>>>
        get() = _usersRanking

    fun fetchUsersRankings() {
        ioScope.launch {
            _usersRanking.postValue(Resource.loading())
            firebaseRepo.fetchAllUsersAdditionalInfo(object : FirebaseUserAddInfoCallback {
                override fun onResponse(response: UsersRankingResponse) {
                    val resultList = response.usersAdditionalInfoList
                    Log.e("user add info", resultList.toString())
                    if (!resultList.isNullOrEmpty()) {
                        val orderedList = RankingController().orderUsersRanking(resultList)
                        _usersRanking.postValue(Resource.success(orderedList))
                    } else {
                        _usersRanking.postValue(
                            Resource.error(
                                "Unable to fetch data from the server",
                                null
                            )
                        )
                    }
                }
            })
        }
    }

}