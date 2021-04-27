package com.example.pucquiz.ui.medals.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.models.Medals
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class MedalsViewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _usersMedals = MutableLiveData<Resource<List<Medals>>>()
    val userMedals: LiveData<Resource<List<Medals>>>
        get() = _usersMedals

    fun fetchUsersMedals() {
        ioScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""
            _usersMedals.postValue(Resource.loading())
            firebaseRepo.fetchUserMedalsByUserId(userId, object : FirebaseUserMedalsCallback {
                override fun onResponse(userMedals: UserMedals?) {
                    val resultList = userMedals?.medals
                    Log.e("user add info", resultList.toString())
                    if (!resultList.isNullOrEmpty()) {
                        _usersMedals.postValue(Resource.success(resultList))
                    } else {
                        _usersMedals.postValue(
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