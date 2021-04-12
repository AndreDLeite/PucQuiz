package com.example.pucquiz.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pucquiz.shared.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class SplashViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    private val _loggedUserLiveDataResult = MutableLiveData<Resource<Boolean>>()
    val loggedUserLiveDataResult: LiveData<Resource<Boolean>>
        get() = _loggedUserLiveDataResult

    init {
        checkLoggedInUser()
    }

    private fun checkLoggedInUser() {
        ioScope.launch {
            _loggedUserLiveDataResult.postValue(Resource.loading())
            FirebaseAuth.getInstance().currentUser?.apply {
                _loggedUserLiveDataResult.postValue(Resource.success(true))
            } ?: run {
                _loggedUserLiveDataResult.postValue(Resource.success(false))
            }
        }
    }

}