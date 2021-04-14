package com.example.pucquiz.ui.mainboard.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.repositories.firebasertdb.IFirebaseRTDBRepository
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_INFO_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_MEDALS_BUCKET
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class UserInfoVewModel(
    application: Application,
    private val firebaseRepo: IFirebaseRTDBRepository
) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _currentUserInfo = MutableLiveData<Resource<User>>()
    val currentUserInfo: LiveData<Resource<User>>
        get() = _currentUserInfo

    private val _currentUserAdditionalInfo = MutableLiveData<Resource<UserAdditionalInfo>>()
    val currentUserAdditionalInfo: LiveData<Resource<UserAdditionalInfo>>
        get() = _currentUserAdditionalInfo

    private val _currentUserMedals = MutableLiveData<Resource<UserMedals>>()
    val currentUserMedals: LiveData<Resource<UserMedals>>
        get() = _currentUserMedals

    init {
        //TODO: Change this logic, since the data can always change, send help
        fetchUserData()
        fetchUserAdditionalData()
        fetchUserMedalsData()
    }

    private fun fetchUserData() {
        ioScope.launch {
            _currentUserInfo.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""

            firebaseRepo.fetchUserInfoByUserId(userId, object : FirebaseUserCallback {
                override fun onResponse(response: User?) {
                    response?.let {
                        _currentUserInfo.postValue(Resource.success(it))
                    } ?: run {
                        _currentUserInfo.postValue(Resource.error("unable to fetch user data from server", null))
                    }
                }
            })
        }
    }

    private fun fetchUserAdditionalData() {
        ioScope.launch {
            _currentUserInfo.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""

            firebaseRepo.fetchUserAdditionalInfoByUserId(userId, object: FirebaseUserAddInfoCallback {
                override fun onResponse(response: UsersRankingResponse) {
                    response.usersAdditionalInfoList?.first()?.let {
                        _currentUserAdditionalInfo.postValue(Resource.success(it))
                    } ?: run {
                        _currentUserAdditionalInfo.postValue(Resource.error("unable to fetch user data from server", null))
                    }
                }
            })
        }
    }

    private fun fetchUserMedalsData() {
        ioScope.launch {
            _currentUserMedals.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid ?: ""

            firebaseRepo.fetchUserMedalsByUserId(userId, object : FirebaseUserMedalsCallback {
                override fun onResponse(userMedals: UserMedals?) {
                    userMedals?.let {
                        _currentUserMedals.postValue(Resource.success(userMedals))
                    } ?: run {
                        _currentUserMedals.postValue(Resource.error("unable to fetch user medals from server", null))
                    }
                }

            })
        }
    }

}