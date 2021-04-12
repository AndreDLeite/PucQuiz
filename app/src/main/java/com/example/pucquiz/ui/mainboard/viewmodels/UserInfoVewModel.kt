package com.example.pucquiz.ui.mainboard.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.models.User
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_BUCKET
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

    private val _currentLoggedUser = MutableLiveData<Resource<User>>()
    val currentLoggedUser: LiveData<Resource<User>>
        get() = _currentLoggedUser

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        ioScope.launch {
            _currentLoggedUser.postValue(Resource.loading())
            val user = FirebaseAuth.getInstance().currentUser
            val reference = FirebaseDatabase.getInstance().getReference(FIREBASE_USER_BUCKET)
            val userId = user?.uid ?: ""

            reference.child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)
                        Log.d("user", "$userProfile")
                        userProfile?.let {
                            _currentLoggedUser.postValue(Resource.success(userProfile))
                        } ?: run {
                            _currentLoggedUser.postValue(
                                Resource.error(
                                    "error casting user from firebase",
                                    null
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        _currentLoggedUser.postValue(
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