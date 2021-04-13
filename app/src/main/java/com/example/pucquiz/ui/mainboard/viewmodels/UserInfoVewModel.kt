package com.example.pucquiz.ui.mainboard.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_BUCKET
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

class UserInfoVewModel(application: Application) : AndroidViewModel(application), KoinComponent {

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
        fetchUserData()
        fetchUserAdditionalData()
        fetchUserMedalsData()
    }

    private fun fetchUserData() {
        ioScope.launch {
            _currentUserInfo.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val reference = FirebaseDatabase.getInstance().getReference(FIREBASE_USER_BUCKET)
            val userId = user?.uid ?: ""

            reference.child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)
                        Log.d("user", "$userProfile")
                        userProfile?.let {
                            _currentUserInfo.postValue(Resource.success(userProfile))
                        } ?: run {
                            _currentUserInfo.postValue(
                                Resource.error(
                                    "error casting user from firebase",
                                    null
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _currentUserInfo.postValue(
                            Resource.error(
                                "error loading user data",
                                null
                            )
                        )
                    }

                }
            )
        }
    }

    private fun fetchUserAdditionalData() {
        ioScope.launch {
            _currentUserInfo.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val reference = FirebaseDatabase.getInstance().getReference(FIREBASE_USER_INFO_BUCKET)
            val userId = user?.uid ?: ""

            reference.child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userAdditionalInfo = snapshot.getValue(UserAdditionalInfo::class.java)
                        Log.d("user addinfo", "$userAdditionalInfo")
                        userAdditionalInfo?.let {
                            _currentUserAdditionalInfo.postValue(Resource.success(userAdditionalInfo))
                        } ?: run {
                            _currentUserAdditionalInfo.postValue(
                                Resource.error(
                                    "error casting user from firebase",
                                    null
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _currentUserAdditionalInfo.postValue(
                            Resource.error(
                                "error loading user data",
                                null
                            )
                        )
                    }

                }
            )
        }
    }

    private fun fetchUserMedalsData() {
        ioScope.launch {
            _currentUserInfo.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val reference = FirebaseDatabase.getInstance().getReference(FIREBASE_USER_MEDALS_BUCKET)
            val userId = user?.uid ?: ""

            reference.child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userMedals = snapshot.getValue(UserMedals::class.java)
                        Log.d("user medals", "$userMedals")
                        userMedals?.let {
                            _currentUserMedals.postValue(Resource.success(userMedals))
                        } ?: run {
                            _currentUserMedals.postValue(
                                Resource.error(
                                    "error casting user from firebase",
                                    null
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _currentUserMedals.postValue(
                            Resource.error(
                                "error loading user data",
                                null
                            )
                        )
                    }

                }
            )
        }
    }

}